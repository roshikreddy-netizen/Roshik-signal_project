
package com.alerts.decorator;

import com.alerts.Alert;
/**
 * Decorator that marks alerts for repeated checking.
 *
 * This adds behavior dynamically without changing Alert or AlertGenerator,
 * supporting Open/Closed Principle and Single Responsibility Principle.
 *
 * It is useful for critical conditions that must be re-evaluated over time.
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    public RepeatedAlertDecorator(Alert alert) {
        super(alert);
    }

    
    public String getCondition() {
        return wrappedAlert.getCondition() + " [RECHECK REQUIRED]";
    }
}