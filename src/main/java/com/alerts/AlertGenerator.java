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
 */
public class AlertGenerator {

    private DataStorage dataStorage;
    private List<AlertRule> rules;

    private List<Alert> emittedAlerts = new ArrayList<>();

    public AlertGenerator(DataStorage dataStorage, List<AlertRule> rules) {
        this.dataStorage = dataStorage;
        this.rules = rules;
    }

    /**
 * This is a convenient constructor that initializes the AlertGenerator with a default set of rules.
 *
 * This constructor is provided to maintain compatibility with existing code
 * that only supplies a DataStorage object.
 *
 * It prevents breaking changes while still enforcing the SOLID design in the system,
 * since the full constructor with explicit AlertRule call remains as the main approach.
 */
public AlertGenerator(DataStorage dataStorage) {
    this.dataStorage = dataStorage;

    /**
     * Default rule configuration ensures the system remains functional
     * without requiring external rule injection.
     *
     * This keeps it usable while still allowing full extension
     * through the main constructor that accepts custom rules.
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
     * Evaluates all patients using all rules.
     */
    public void evaluateData() {

        for (Patient patient : dataStorage.getAllPatients()) {

            for (AlertRule rule : rules) {

                List<Alert> alerts = rule.evaluate(patient);

                for (Alert alert : alerts) {
                    triggerAlert(alert);
                }
            }
        }
    }

    private void triggerAlert(Alert alert) {
        emittedAlerts.add(alert);

        System.out.println(
                "[ALERT] Patient=" + alert.getPatientId() +
                " Condition=" + alert.getCondition()
        );
    }

    public List<Alert> getEmittedAlerts() {
        return emittedAlerts;
    }
}