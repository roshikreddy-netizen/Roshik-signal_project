package com.alerts.rules;

import com.alerts.Alert;
import com.alerts.rules.AlertRule;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic threshold-based rule.
 */
public class ThresholdRule implements AlertRule {

    private String vitalType;
    private double minValue;
    private double maxValue;

    public ThresholdRule(String vitalType, double minValue, double maxValue) {
        this.vitalType = vitalType;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public List<Alert> evaluate(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord record : patient.getRecords(0, Long.MAX_VALUE)) {

            if (record.getRecordType().equals(vitalType)) {

                double value = record.getMeasurementValue();

                if (value < minValue || value > maxValue) {
                    alerts.add(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "THRESHOLD_BREACH: " + vitalType,
                            System.currentTimeMillis()
                    ));
                }
            }
        }

        return alerts;
    }
}
