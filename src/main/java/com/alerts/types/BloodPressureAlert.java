package com.alerts.types;

import com.alerts.Alert;

/**
 * Represents alerts related to blood pressure abnormalities.
 *
 * <p>This subclass allows categorization of alerts by type, making it easier
 * to extend behavior in the future (e.g., specialized handling or routing).</p>
 */
public class BloodPressureAlert extends Alert {

    /**
     * Constructs a blood pressure alert.
     */
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
