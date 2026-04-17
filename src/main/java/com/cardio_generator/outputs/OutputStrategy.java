package com.cardio_generator.outputs;

/**
 * Decides how generated patient data is delivered/output.
 * 
 * <p>
 * Implementation of this interface decides which and how 
 * the generated cardiovascular data is sent. 
 * </p>
 * @author Roshik
 */
public interface OutputStrategy {

    /**
     * Outputs patient data according to the strategy implemented.
     *
     * @param patientId The unique identifier of the patient.
     * @param timestamp The time the measurement was recorded, in milliseconds since epoch.
     * @param label A descriptive label for the measurement (like HeartRate).
     * @param data The actual measurement value as a string.
     */
    void output(int patientId, long timestamp, String label, String data);
}
