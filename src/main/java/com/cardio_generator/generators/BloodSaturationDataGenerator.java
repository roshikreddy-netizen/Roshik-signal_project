package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated blood oxygen saturation (SpO2) levels for multiple patients.
 * <p>
 * Each patient is assigned a baseline saturation value between 95% and 100% at initialization.
 * Future values fluctuate slightly to match real-world.
 * <p>
 * The generator ensures that all saturation values remain within a realistic range (90%-100%).
 * The generated values are sent to a specified {@link OutputStrategy} for handling.
 * 
 * @author Roshik
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Constructs a BloodSaturationDataGenerator for a given number of patients.
     * Initializes each patient's baseline saturation level to a random value between 95% and 100%.
     * 
     * @param patientCount the total number of patients to simulate (1-based indexing)
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates a new blood saturation value for the specified patient and outputs it via the given {@link OutputStrategy}.
     * <p>
     * The new value is calculated by applying a small random variation (-1, 0, or 1) to the last recorded value.
     * The result is clamped between 90% and 100% to ensure realistic readings.
     * 
     * @param patientId the ID of the patient for whom to generate data (must be between 1 and patientCount)
     * @param outputStrategy the output strategy responsible for handling the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;

            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}