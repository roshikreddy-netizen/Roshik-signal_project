package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects abnormal heart rates.
 *
 * Assumption made:
 * - Heart rate records use type "HeartRate"
 */
public class HeartRateStrategy implements AlertStrategy {

    /**
     * Checks for dangerous heart rates.
     *
     * 
     * - Separates algorithm from alert system
     * - Improves maintainability
     */
    @Override
    public List<Alert> checkAlert(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> records =
                patient.getRecords(0, Long.MAX_VALUE);

        for (PatientRecord record : records) {

            if (!record.getRecordType().equals("HeartRate")) {
                continue;
            }

            double value = record.getMeasurementValue();

            /**
             * Assumption made:
             * - Below 40 = bradycardia
             * - Above 140 = tachycardia
             */
            if (value < 40 || value > 140) {

                alerts.add(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Abnormal Heart Rate",
                        record.getTimestamp()
                ));
            }
        }

        return alerts;
    }
}
