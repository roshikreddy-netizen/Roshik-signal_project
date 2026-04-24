package com.alerts.rules;

import com.data_management.Patient;
import java.util.List;

import com.alerts.*;

/**
 * Defines a rule for generating alerts.
 */
public interface AlertRule {

    /**
     * Evaluates patient data and returns alerts if conditions are met.
     */
    List<Alert> evaluate(Patient patient);
}