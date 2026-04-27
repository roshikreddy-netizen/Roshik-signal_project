package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.Patient;
import java.util.List;

/**
 * Strategy interface for alert detection algorithms.
 *
 * This defines a common contract for all alert-checking logic.
 * It allows AlertGenerator to switch between different monitoring behaviors at runtime.
 *
 * Each implementation focuses on one specific health metric,
 * keeping the system modular and easy to extend.
 */
public interface AlertStrategy {

    /**
     * Checks if a patient meets conditions for triggering alerts.
     *
     * @param patient the patient whose data is being analyzed
     * @return list of alerts triggered by this strategy
     */
    List<Alert> checkAlert(Patient patient);
}
