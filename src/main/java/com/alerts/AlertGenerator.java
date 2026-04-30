package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.alerts.rules.AlertRule;
import com.alerts.rules.CombinedRule;
import com.alerts.rules.ThresholdRule;
import com.alerts.rules.TrendRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates alert rule execution.
 *
 * Before, this class contained hardcoded logic for checking conditions.
 * Has been changed to assign all alert logic to AlertRule classes,
 * improving modularity and following the Strategy pattern.
 */
public class AlertGenerator {

    /** Central data source for all patient records */
    private DataStorage dataStorage;

    /**
     * List of alert rules used to evaluate patient data.
     *
     * This replaces the old hardcoded methods (e.g., checkBloodPressure),
     * allowing behavior to be changed dynamically without modifying this class.
     */
    private List<AlertRule> rules;

    /** Stores all alerts generated during evaluation */
    private List<Alert> emittedAlerts = new ArrayList<>();

    /**
     * Primary constructor using dependency injection.
     *
     * This was introduced to follow SOLID principles (especially OCP and DIP),
     * allowing external configuration of rules instead of hardcoding them.
     */
    public AlertGenerator(DataStorage dataStorage, List<AlertRule> rules) {
        this.dataStorage = dataStorage;
        this.rules = rules;
    }

    /**
     * Convenience constructor with default rules.
     *
     * This was added to maintain backward compatibility with older code
     * that only passed DataStorage.
     *
     * It ensures the system still works out-of-the-box while supporting
     * more flexible rule injection via the main constructor.
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;

        /**
         * Default rule configuration.
         *
         * These rules replace the old internal logic methods and represent
         * the system’s core alert checks in a modular way.
         */
        this.rules = List.of(
                new ThresholdRule("SystolicPressure", 90, 180),
                new ThresholdRule("DiastolicPressure", 60, 120),
                new CombinedRule(),
                new TrendRule("SystolicPressure"),
                new TrendRule("DiastolicPressure")
        );
    }

    /**
     * Evaluates all patients using the configured rules.
     *
     * This replaces the old approach where each condition was manually checked.
     * Now, each rule independently decides if an alert should be triggered.
     */
    public void evaluateData() {
        for (Patient patient : dataStorage.getAllPatients()) {
            for (AlertRule rule : rules) {
                emittedAlerts.addAll(rule.evaluate(patient));
            }
        }
    }

    /**
     * Returns all generated alerts.
     *
     * This allows external systems (e.g., UI, logging, tests)
     * to access alert results without tightly coupling to this class.
     */
    public List<Alert> getEmittedAlerts() {
        return emittedAlerts;
    }
}