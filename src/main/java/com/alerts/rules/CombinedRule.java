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

        boolean lowBP = false;
        boolean lowSat = false;

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        for (PatientRecord r : records) {

            if (r.getRecordType().equals("SystolicPressure")
                    && r.getMeasurementValue() < 90) {
                lowBP = true;
            }

            if (r.getRecordType().equals("Saturation")
                    && r.getMeasurementValue() < 92) {
                lowSat = true;
            }
        }

        List<Alert> alerts = new ArrayList<>();

        if (lowBP && lowSat) {
            alerts.add(new Alert(
                    String.valueOf(patient.getPatientId()),
                    "HYPOTENSIVE_HYPOXEMIA",
                    System.currentTimeMillis()
            ));
        }

        return alerts;
    }
}