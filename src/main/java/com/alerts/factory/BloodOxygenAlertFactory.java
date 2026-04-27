package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.types.BloodOxygenAlert;

/**
 * Factory responsible for creating blood oxygen alerts.
 *
 * Used by rules such as LowSaturationRule to generate alerts
 * without directly instantiating Alert objects.
 *
 * This improves modularity by keeping alert creation logic separate
 * from condition-checking logic.
 */
public class BloodOxygenAlertFactory extends AlertFactory {
@Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "ECG_ALERT: " + condition, timestamp);
    }
}