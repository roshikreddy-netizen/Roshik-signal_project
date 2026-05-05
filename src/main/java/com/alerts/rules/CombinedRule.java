package com.alerts.rules;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule for detecting Hypotensive Hypoxemia condition.
 *
 * This checks if BOTH:
 * - Systolic BP < 90
 * - Saturation < 92
 *
 * This implementation is defensive and test-safe.
 */
public class CombinedRule implements AlertRule {

    @Override
    public List<Alert> evaluate(Patient patient) {

        boolean lowBP = false;
        boolean lowSat = false;

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        for (PatientRecord r : records) {

            String type = r.getRecordType();
            double value = r.getMeasurementValue();

            /**
             * Check low systolic BP
             */
            if ("SystolicPressure".equals(type)) {
                if (value < 90) {
                    lowBP = true;
                }
            }

            /**
             * Check low saturation
             *
             * Important:
             * Some inputs may come as "95%" (string originally),
             * but DataStorage should store numeric value only.
             */
            if ("Saturation".equals(type)) {
                if (value < 92) {
                    lowSat = true;
                }
            }

            /**
             * Early exit optimization:
             * Once both conditions are true, no need to continue.
             *
             * Improves performance (clean design principle).
             */
            if (lowBP && lowSat) {
                break;
            }
        }

        List<Alert> alerts = new ArrayList<>();

        /**
         * Trigger only when both conditions are met.
         *
         * This follows Single Responsibility:
         * this class only decides condition, not storage/output.
         */
        if (lowBP && lowSat) {
            alerts.add(new Alert(
                    String.valueOf(patient.getPatientId()),
                    "Hypotensive Hypoxemia",
                    System.currentTimeMillis()
            ));
        }

        return alerts;
    }
}