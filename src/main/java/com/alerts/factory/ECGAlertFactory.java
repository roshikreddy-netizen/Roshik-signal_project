package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.types.ECGAlert;

/**
 * Factory responsible for creating ECG-related alerts.
 *
 * Integrated into the system through ECGRule, which calls this factory
 * when abnormal ECG patterns are detected.
 *
 * This allows ECG alert logic to evolve independently from rule logic.
 */
public class ECGAlertFactory extends AlertFactory {

    /**
     * Creates an ECGAlert instance.
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
