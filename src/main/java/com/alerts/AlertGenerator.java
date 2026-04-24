package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.alerts.rules.AlertRule;
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