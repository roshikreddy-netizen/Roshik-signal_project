package data_management;
import org.junit.jupiter.api.Test;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class PatientTest {

      /**
     * Tests that records are actually stored in patient object.
     */
    @Test
    void testAddRecord() {

        Patient patient = new Patient(1);

        patient.addRecord(120, "SystolicPressure", 1000L);
        patient.addRecord(80, "DiastolicPressure", 2000L);

        List<PatientRecord> records =
                patient.getRecords(0L, Long.MAX_VALUE);

        assertEquals(2, records.size(),
                "Patient should store 2 records");
    }

    /**
     * Ensures time filtering works correctly.
     * Tests whether time filtering works correctly.
     *
     * Only records inside the time range should be returned.
     */
    @Test
    void testGetRecordsTimeFilter() {

        Patient patient = new Patient(1);

        patient.addRecord(100, "ECG", 1000L);
        patient.addRecord(110, "ECG", 5000L);

        List<PatientRecord> records =
                patient.getRecords(2000L, 6000L);

        assertEquals(1, records.size(),
                "Only one record should match time range");
    }
    
}
