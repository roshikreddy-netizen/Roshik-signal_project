package com.alerts;

import java.util.List;

/**
 * AlertManager is responsible only for dispatching alerts.
 * It does not decide when alerts are created.
 */
public class AlertManager {

    /**
     * Sends alert to external system (console, hospital system, etc.)
     */
    public void dispatch(Alert alert) {
        System.out.println("[ALERT DISPATCHED] " +
                "Patient=" + alert.getPatientId() +
                " Condition=" + alert.getCondition() +
                " Time=" + alert.getTimestamp());
    }

    /**
     * Optional routing logic
     */
    public void routeToStaff(Alert alert) {
        // Placeholder for staff logic
    }
}