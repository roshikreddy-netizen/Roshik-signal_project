package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for monitoring blood pressure.
 *
 * This strategy encapsulates all logic related to blood pressure,
 * including threshold checks and trend detection.
 *
 * It integrates into the system by being used inside AlertGenerator,
 * allowing blood pressure logic to be swapped or updated independently.
 */
public class BloodPressureStrategy implements AlertStrategy {

    @Override
    public List<Alert> checkAlert(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord r : patient.getRecords(0, Long.MAX_VALUE)) {

            if (r.getRecordType().equals("SystolicPressure")) {

                double value = r.getMeasurementValue();

                // Critical threshold check
                if (value < 90 || value > 180) {
                    alerts.add(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "BloodPressureCritical",
                            System.currentTimeMillis()
                    ));
                }
            }
        }

        return alerts;
    }
}
