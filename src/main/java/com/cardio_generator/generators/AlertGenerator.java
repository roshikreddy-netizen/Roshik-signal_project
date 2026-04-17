package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alerts for patients using a probability based model.
 * Each patient can have an alert triggered or resolved.
 * <p>
 * This class keeps track of alert states for each patient and
 * simulates alerts with based on probabilities.
 * Alerts can either be "triggered" or "resolved" over time.
 * </p>
 * 
 * @author Roshik
 */
public class AlertGenerator implements PatientDataGenerator {

    /** Random number generator used for probabilistic alert generation */
    public static final Random RANDOM_GENERATOR = new Random();

    /** 
     * Stores the alert state for each patient.
     * false = resolved, true = pressed/active
     */
    private boolean[] alertStates;

    /**
     * Constructor to initialize alert states for patients.
     *
     * @param patientCount the number of patients to simulate
     */
    public AlertGenerator(int patientCount) {
        // Variable name changed to camelCase
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates an alert for the specified patient.
     * <p>
     * If the patient currently has an active alert, there is a 90% chance
     * the alert will be resolved. If the patient has no active alert, there
     * is a probability-based chance of triggering a new alert.
     * </p>
     *
     * @param patientId the ID of the patient
     * @param outputStrategy the strategy used to output the alert data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Lambda variable name changed to lowercase
                double lambda = 0.1; // Average rate (alerts per period)
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            // Proper error handling with informative message
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}