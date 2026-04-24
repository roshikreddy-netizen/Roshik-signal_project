package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;

    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }
    
    /** getPatientID gives access to the private patientID field
     * In the alert system, we use the patientId from Patient to label alerts, 
     * while PatientRecord uses its own stored 
     * patientId to represent individual measurements. 
     * This separation is correct because the Patient ID identifies the person, 
     * while PatientRecord just repeats that ID to 
     * link each measurement back to the correct patient.
      */

    public int getPatientId(){
        return patientId;
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and
     * timestamp.
     *
     * @param measurementValue the measurement value to store in the record
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since UNIX epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within a
     * specified time range.
     * The method filters records based on the start and end times provided.
     *
     * @param startTime the start of the time range, in milliseconds.
     * @param endTime   the end of the time range, in milliseconds 
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> filteredRecords = new ArrayList<>();
        /**Logic:
         *  Iterates through all stored records for a patient.
         *  Checks each record's timestamp.
         * Includes the record only if it is between startTime and endTime (inclusive).
         *
         * Assumptions:
         * patientRecords contains all historical records for the patient.
         * timestamps are stored in milliseconds.
         * Records are not necessarily sorted, so full iteration is required.
         */
    for (PatientRecord record : patientRecords) {

        long timestamp = record.getTimestamp();

        if (timestamp >= startTime && timestamp <= endTime) {
            filteredRecords.add(record);
        }
    }

    return filteredRecords;
    }
}
