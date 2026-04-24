package com.alerts.rules;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects increasing or decreasing trends in a vital sign.
 */
public class TrendRule implements AlertRule {

    private String vitalType;

    public TrendRule(String vitalType) {
        this.vitalType = vitalType;
    }

    @Override
    public List<Alert> evaluate(Patient patient) {

        List<Double> values = new ArrayList<>();
        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord r : patient.getRecords(0, Long.MAX_VALUE)) {
            if (r.getRecordType().equals(vitalType)) {
                values.add(r.getMeasurementValue());
            }
        }

        if (values.size() < 3) return alerts;

        for (int i = 2; i < values.size(); i++) {

            double a = values.get(i - 2);
            double b = values.get(i - 1);
            double c = values.get(i);

            boolean increasing = (b - a > 10) && (c - b > 10);
            boolean decreasing = (a - b > 10) && (b - c > 10);

            if (increasing || decreasing) {
                alerts.add(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "TREND_ALERT: " + vitalType,
                        System.currentTimeMillis()
                ));
                break;
            }
        }

        return alerts;
    }
}
