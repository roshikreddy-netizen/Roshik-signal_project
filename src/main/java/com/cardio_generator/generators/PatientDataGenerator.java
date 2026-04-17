package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Represents a generator for patient health data.
 * Implementations of this interface are responsible for generating
 * specific types of measurements (like heart rate, blood pressure)
 * and sending them to a specified output strategy.
 * 
 * Usage example:
 * <pre>{@code
 * PatientDataGenerator generator = new HeartRateGenerator();
 * generator.generate(patientId, fileOutputStrategy);
 * }</pre>
 * 
 * Implementations should define how the data is generated and formatted.
 * 
 * @author Roshik
 */
public interface PatientDataGenerator {

    /**
     * Generates patient data for a specific patient and sends it to the given output strategy.
     * Implementations should handle the details of measurement generation and output formatting.
     *
     * @param patientId the unique identifier of the patient for whom the data is generated
     * @param outputStrategy the strategy to which the generated data will be sent
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}