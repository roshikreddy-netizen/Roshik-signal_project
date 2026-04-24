package com;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

import java.io.IOException;

/**
 * Entry point for the application.
 *
 * Allows switching between simulation mode and data analysis mode
 * using command-line arguments.
 *
 * Usage:
 * - Default: runs HealthDataSimulator
 * - "DataStorage": runs DataStorage logic
 */
public class Main {

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("DataStorage")) {

            System.out.println("Running DataStorage mode...");
            DataStorage.main(new String[]{});

        } else {

            System.out.println("Running HealthDataSimulator mode...");

            try {
                HealthDataSimulator.main(args); // FIX: wrapped in try-catch
            } catch (IOException e) {
                System.err.println("Error running HealthDataSimulator: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}