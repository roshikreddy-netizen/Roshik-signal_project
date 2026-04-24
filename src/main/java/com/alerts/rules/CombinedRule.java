package com.alerts.rules;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects dangerous combined conditions.
 * Dangerous combined condition:
 * - systolic < 90
 * - oxygen < 92%
 */
public class CombinedRule implements AlertRule {
 @Override
    public List<Alert> evaluate(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        Double latestSystolic = null;
        Double latestOxygen = null;
        long latestTime = System.currentTimeMillis();

        for (PatientRecord r : records) {

            if (r.getRecordType().equals("SystolicPressure")) {
                latestSystolic = r.getMeasurementValue();
                latestTime = r.getTimestamp();
            }

            if (r.getRecordType().equals("Saturation")) {
                latestOxygen = r.getMeasurementValue();
                latestTime = r.getTimestamp();
            }
        }

        if (latestSystolic != null && latestOxygen != null) {

            if (latestSystolic < 90 && latestOxygen < 92) {
                alerts.add(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "HYPOTENSIVE_HYPOXEMIA",
                        latestTime
                ));
            }
        }

        return alerts;
    }
   
}