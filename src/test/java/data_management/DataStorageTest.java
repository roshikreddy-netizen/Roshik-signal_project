package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;
/**Unit test of DataStorage 
 * 
 * These verify that patient data is: 
 * - stored 
 * -retrieved 
 * -filtered by time range 
 * 
 * This ensures the central repository of medical data behaves correctly.
 */
class DataStorageTest {
/**
 * Tets that patient data can be added and retrieved correctly. 
 * 
 * Verifies: 
 * - DataStorage correctly store multiple records
 * - Patient records are retrievable by patient ID
 */
    @Test
    void testAddAndGetRecords() {
        DataStorage storage = new DataStorage();

        // These calls won't actually store anything in the current DataStorage
        storage.addPatientData(1, 120.0, "SystollicPressure", 1000L);
        storage.addPatientData(1, 80.0, "DiastollicPressure", 2000L);
        // records will be empty; don't access index 0
        // List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        // assertEquals(2, records.size());
        // assertEquals(100.0, records.get(0).getMeasurementValue());
        List<PatientRecord> records = storage.getRecords(1, 0L, Long.MAX_VALUE);

        assertEquals(2, records.size(), "Storage should return 2 records for patient 1");
        
    }

    @Test 
    void testGetRecordsForUnkownPatient(){
        DataStorage storage = new DataStorage();

        List<PatientRecord> records = storage.getRecords(999, 0L, Long.MAX_VALUE);

        assertNotNull(records, "System should return empty lsit, not null");
        assertTrue(records.isEmpty(),"Unknown patient should have no records");
    }
}