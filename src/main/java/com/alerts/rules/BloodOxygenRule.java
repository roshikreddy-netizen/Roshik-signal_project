package com.alerts.rules;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles oxygen saturation alerts:
 * - Low oxygen (<92%)
 * - Rapid drop (>=5% within 10 min)
 */
public class BloodOxygenRule implements AlertRule {

    @Override
    public List<Alert> evaluate(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        List<PatientRecord> oxygen = new ArrayList<>();

        for (PatientRecord r : records) {
            if (r.getRecordType().equals("Saturation")) {
                oxygen.add(r);
            }
        }

        oxygen.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));

        // LOW SATURATION
        for (PatientRecord r : oxygen) {
            if (r.getMeasurementValue() < 92) {
                alerts.add(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Low Oxygen Saturation",
                        r.getTimestamp()
                ));
            }
        }

        // RAPID DROP (within 10 min)
        for (int i = 1; i < oxygen.size(); i++) {

            PatientRecord prev = oxygen.get(i - 1);
            PatientRecord curr = oxygen.get(i);

            long diff = curr.getTimestamp() - prev.getTimestamp();

            double drop = prev.getMeasurementValue() - curr.getMeasurementValue();

            if (diff <= 10 * 60 * 1000 && drop >= 5) {
                alerts.add(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Rapid Oxygen Drop",
                        curr.getTimestamp()
                ));
            }
        }

        return alerts;
    }
}
