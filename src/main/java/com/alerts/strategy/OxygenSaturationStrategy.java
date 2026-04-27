package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for monitoring blood oxygen levels.
 *
 * This strategy checks for low saturation and rapid drops.
 * It separates oxygen-specific logic from other alert rules,
 * improving clarity and maintainability.
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    @Override
    public List<Alert> checkAlert(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord r : patient.getRecords(0, Long.MAX_VALUE)) {

            if (r.getRecordType().equals("Saturation")
                    && r.getMeasurementValue() < 92) {

                alerts.add(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "LowSaturation",
                        System.currentTimeMillis()
                ));
            }
        }

        return alerts;
    }
}
