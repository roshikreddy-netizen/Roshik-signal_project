package com.alerts.types;

import com.alerts.Alert;

/**
 * Represents alerts related to blood oxygen levels.
 *
 * <p>This class exists to clearly separate oxygen-related alerts from
 * other alert types and support future extensions.</p>
 */
public class BloodOxygenAlert extends Alert {

    public BloodOxygenAlert(String patientId,
                            String condition,
                            long timestamp) {

        super(patientId, condition, timestamp);
    }
}