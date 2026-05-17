package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alerts.Alert;
import com.alerts.AlertGenerator;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */



public class DataStorage {

    /** Singleton shared instance of DataStorage
     * 
     * - Prevents multiple inconsistent sotrage objects 
     * -Provide gloabl access point
     */
    private static DataStorage instance;

    /*Stores patient objects indexed by their unique patient ID. 
    It is thread safe, WebSocket client runs in separate thread */
    private Map<Integer, Patient> patientMap; 
    
    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public DataStorage() {
        this.patientMap = new ConcurrentHashMap<>();
    }

    /**
     * Returns the shared DataStorage instance.
     *
     * why:
     * - Ensures all components use same storage object
     * - synchronized prevents race conditions during creation
     */
    public static synchronized DataStorage getInstance() {

        if (instance == null) {
            instance = new DataStorage();
        }

        return instance;
    }


    /**
     * Adds or updates patient data in the storage.
     * This implementation uses ConcurrentHashMap's computeIfAbsent 
     * to safely initialize patients without explicit synchronization.
     * It avoids race conditions while keeping the code concise.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since the Unix epoch
     * synchronized: ensures only one thread updates are a time
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {

    patientMap
        .computeIfAbsent(patientId, Patient::new)
        .addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by
     * a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be
     *                  retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix
     *                  epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors
     * and evaluates patient data.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // DataReader is not defined in this scope, should be initialized appropriately.
        // DataReader reader = new SomeDataReaderImplementation("path/to/data");

        /**
         * Singleton access replaces constructor usage.
         */
        DataStorage storage = DataStorage.getInstance();

        // Assuming the reader has been properly initialized and can read data into the
        // storage
        // reader.readData(storage);

         /**
         * Singleton access replaces constructor usage.
         */


        // Example of using DataStorage to retrieve and print records for a patient
        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
        for (PatientRecord record : records) {
            System.out.println("Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }
            /* NOTE:
                Alert generation logic was removed from this class to follow the single responsibility principle.
                DataStorage is responsible only for storing and retrieving patient data, not processing it.
                Alert evaluation is now handled in a separate class (HealthSystemRunner). 
            */
               
                
       // Initialize the AlertGenerator with the storage
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        /**
         * Evaluate all patient data.
         *
         * The AlertGenerator internally applies all strategies
         * and checks every patient in the DataStorage.
         *
         * No loop is required because the Strategy pattern
         * places all evaluation process inside AlertGenerator.
         */
        alertGenerator.evaluateData();

      /**
       * <p>
       *The for loop above for "alertGenerator.evaluateData(patient)" shall be rpelaced
       * with "alertGenerator.evaluateData()". The method was changed so that AlertGenerator 
       * evaluates all patients directly from DataStorage instead of being passed one patient at a time. 
       * Because of this change, the old call that passes a Patient object no longer matches the new 
       * method signature, which causes the error. Updating the call ensures the code stays consistent 
       * and allows the alert system to properly process all stored patient data in one place.
       */

    }
    

}
