package com.alerts.decorator;


import com.alerts.Alert;
/**
 * Base decorator for Alert objects.
 *
 * This class wraps an existing Alert and allows behavior to be extended
 * without modifying the original Alert class (Open/Closed Principle).
 *
 * It follows the Decorator pattern by delegating all calls to the wrapped object.
 */
public abstract class AlertDecorator extends Alert {

    protected Alert wrappedAlert;

    public AlertDecorator(Alert alert) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
        this.wrappedAlert = alert;
    }
}