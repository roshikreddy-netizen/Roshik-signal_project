package data_management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.PatientRecord;

/**
 * Unit tests for PatientRecord.
 *
 * Ensures that the data model correctly stores and exposes
 * medical measurement information.
 */
class PatientRecordTest {

    /**
     * Checks that a PatientRecord stores all values correctly
     * when it is created.
     */
    @Test
    void testPatientRecordCreation() {

        PatientRecord record =
                new PatientRecord(1, 98.5, "Saturation", 12345L);

        assertEquals(1, record.getPatientId());
        assertEquals(98.5, record.getMeasurementValue());
        assertEquals("Saturation", record.getRecordType());
        assertEquals(12345L, record.getTimestamp());
    }
}
