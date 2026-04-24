package com.alerts.rules;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects abnormal ECG spikes.
 */
public class ECGRule implements AlertRule {
    private static final int WINDOW = 10;

    @Override
    public List<Alert> evaluate(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> ecg = new ArrayList<>();

        for (PatientRecord r : patient.getRecords(0, Long.MAX_VALUE)) {
            if (r.getRecordType().equals("ECG")) {
                ecg.add(r);
            }
        }

        if (ecg.size() < WINDOW) return alerts;

        ecg.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));

        for (int i = WINDOW; i < ecg.size(); i++) {

            List<PatientRecord> window = ecg.subList(i - WINDOW, i);

            double avg = window.stream()
                    .mapToDouble(PatientRecord::getMeasurementValue)
                    .average()
                    .orElse(0);

            PatientRecord current = ecg.get(i);

            // Assumption: spike is 2x above average
            if (current.getMeasurementValue() > avg * 2) {

                alerts.add(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "ABNORMAL_ECG_SPIKE",
                        current.getTimestamp()
                ));
            }
        }

        return alerts;
    }
   
}
