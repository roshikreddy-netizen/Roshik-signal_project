package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * <p>
 * The is the core interface for in the system. It defines a contract 
 * that all patient data generator must follow.
 * </p> 
 * 
 * <p>
 * Implementations of this interface are responsible for generating 
 * medical data for a specific patient and sending it through a 
 * chosen output strategy.
 * </p>
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
     * @return void (data is sent directly to output strategy)
     * @throws IllegalArguementException if patientId <= 0      
     * */
    void generate(int patientId, OutputStrategy outputStrategy);
}