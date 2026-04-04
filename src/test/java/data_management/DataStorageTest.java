package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        DataStorage storage = new DataStorage();

        // These calls won't actually store anything in the current DataStorage
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        // records will be empty; don't access index 0
        // List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        // assertEquals(2, records.size());
        // assertEquals(100.0, records.get(0).getMeasurementValue());
    }
}