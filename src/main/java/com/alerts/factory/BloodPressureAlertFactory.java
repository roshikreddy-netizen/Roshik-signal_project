package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.types.BloodPressureAlert;

/**
 * Factory responsible for creating blood pressure alerts.
 *
 * This integrates with the system by being used inside
 * blood pressure-related rules (e.g., ThresholdRule, TrendRule).
 *
 * It ensures all blood pressure alerts are created in one place,
 * making the system easier to extend and maintain.
 */
public class BloodPressureAlertFactory extends AlertFactory {

    /**
     * Creates a BloodPressureAlert instance.
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
