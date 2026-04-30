package com.alerts.rules;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Triggers alerts when blood pressure goes outside safe medical limits.
 *
 * Assumption:
 * - Systolic and diastolic are stored as separate record types:
 *   "SystolicPressure" and "DiastolicPressure"
 */
public class BloodPressureCriticalThreshold implements AlertRule {

    @Override
    public List<Alert> evaluate(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        for (PatientRecord r : records) {

            String type = r.getRecordType();
            double value = r.getMeasurementValue();

            if (type.equals("SystolicPressure")) {

                if (value > 180 || value < 90) {
                    alerts.add(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "Critical Systolic Pressure",
                            r.getTimestamp()
                    ));
                }
            }

            if (type.equals("DiastolicPressure")) {

                if (value > 120 || value < 60) {
                    alerts.add(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "Critical Diastolic Pressure",
                            r.getTimestamp()
                    ));
                }
            }
        }

        return alerts;
    }
}