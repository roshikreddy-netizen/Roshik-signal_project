package com.alerts.factory;

import com.alerts.Alert;
/**
 * Base factory class for creating Alert objects.
 *
 * This class separates object creation from business logic,
 * allowing AlertGenerator and AlertRules to remain simple and focused.
 *
 * It integrates into the system by being called inside AlertRules
 * whenever an alert condition is detected.
 */
public abstract class AlertFactory {

    /**
     * Creates an Alert object.
     *
     * @param patientId ID of the patient
     * @param condition description of alert condition
     * @param timestamp time of alert
     * @return Alert object
     */
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}
