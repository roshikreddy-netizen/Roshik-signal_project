
package com.alerts.decorator;
/**
 * Decorator that adds priority tagging to alerts.
 *
 * This separates priority handling from core alert logic,
 * following Single Responsibility Principle.
 *
 * It allows urgent alerts to be identified without modifying existing classes.
 */
import com.alerts.Alert;

public class PriorityAlertDecorators extends AlertDecorator {

    public PriorityAlertDecorators(Alert alert) {
        super(alert);
    }

    
    public String getCondition() {
        return "[HIGH PRIORITY] " + wrappedAlert.getCondition();
    }
}
