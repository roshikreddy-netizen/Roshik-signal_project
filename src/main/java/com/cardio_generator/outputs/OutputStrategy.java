package com.cardio_generator.outputs;

/**
 * Givces a strategy for giving out patient data.
 * Implementations of this interface determine how patient health data
 * is recorded, displayed, or transmitted.
 * 
 * @author Roshik
 */
public interface OutputStrategy {

    /**
     * Outputs patient data according to the strategy implementation.
     *
     * @param patientId The unique identifier of the patient.
     * @param timestamp The time the measurement was recorded, in milliseconds since epoch.
     * @param label A descriptive label for the measurement (like HeartRate).
     * @param data The actual measurement value as a string.
     */
    void output(int patientId, long timestamp, String label, String data);
}
