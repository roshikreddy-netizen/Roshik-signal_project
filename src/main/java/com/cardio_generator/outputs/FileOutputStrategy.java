package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An output startegy that writes the patient data to files.
 * <p>
 * Each unique data label gets its own file within the specified base directory. The class
 * uses a ConcurrentHashMap to efficiently manage file paths.
 */
public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name to camelCase
    private String baseDirectory;

    // Variable name changed to camelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a file output strategy. 
     *
     * @param baseDirectory the directory where files will be stored.
     */
    public FileOutputStrategy(String baseDirectory) {
        // Corrected parameter assignment
        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes the patient data to the file corresponding to the data label.
     * If the base directory does not exist, it will be created.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the measurement in milliseconds
     * @param label the type of measurement
     * @param data the actual measurement value as a string
     * 
     * @throws IOException creation id directory fails.
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