package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements an output strategy that writes patient data to files using a concurrent file map.
 * <p>
 * Each unique data label gets its own file within the specified base directory. The class
 * uses a ConcurrentHashMap to efficiently manage file paths for concurrent writes.
 * </p>
 * <p>
 * Usage:
 * <pre>
 * FileOutputStrategy fileOutput = new FileOutputStrategy("data/output");
 * fileOutput.output(patientId, timestamp, "HeartRate", "80");
 * </pre>
 * </p>
 */
public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name to camelCase
    private String baseDirectory;

    // Variable name changed to camelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a FileOutputStrategy that stores files in the specified directory.
     *
     * @param baseDirectory the directory where files will be stored
     */
    public FileOutputStrategy(String baseDirectory) {
        // Corrected parameter assignment
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs patient data to a file corresponding to the given label.
     * <p>
     * If the base directory does not exist, it will be created. Each label is associated
     * with a unique file, and new data is appended to the file in a thread-safe manner.
     * </p>
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the measurement in milliseconds
     * @param label the type of measurement (e.g., "HeartRate")
     * @param data the actual measurement value as a string
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create directory 
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        // Changed variable name to camelCase
        String filePath = fileMap.computeIfAbsent(label, k -> 
            Paths.get(baseDirectory, label + ".txt").toString()
        );

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}