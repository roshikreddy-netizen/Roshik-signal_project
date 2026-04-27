package com.alerts.types;

import com.alerts.Alert;

/**
 * Represents alerts triggered by abnormal ECG readings.
 *
 * <p>Used for identifying irregular heart activity such as spikes
 * or abnormal patterns.</p>
 */
public class ECGAlert extends Alert {

    /**
     * Constructs an ECG alert.
     */
    public ECGAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}