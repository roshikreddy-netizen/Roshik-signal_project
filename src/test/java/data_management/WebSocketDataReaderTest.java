package data_management;

import com.data_management.DataStorage;
import com.data_management.WebSocketDataReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WebSocketDataReader.
 *
 * These tests focus on message parsing, ensuring that valid messages
 * are correctly stored and invalid inputs are safely ignored.
 */
class WebSocketDataReaderTest {

    /**
     * Tests valid message parsing.
     *
     * Edge case handled:
     * - Ensures correct parsing of numeric values
     * - Ensures data is stored in DataStorage
     */
    @Test
    void testValidMessageParsing() {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        reader.handleMessage("1,1000,SystolicPressure,120", storage);

        assertEquals(1, storage.getAllPatients().size(),
                "Valid message should create a patient record");
    }

    /**
     * Tests percentage parsing (e.g., 98%).
     *
     * Edge case:
     * - Ensures '%' symbols do not break parsing
     */
    @Test
    void testPercentageParsing() {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        reader.handleMessage("1,1000,Saturation,98%", storage);

        assertFalse(storage.getAllPatients().isEmpty(),
                "Percentage values should be parsed correctly");
    }

    /**
     * Tests invalid message format.
     *
     * Edge case:
     * - Missing fields should NOT crash system
     */
    @Test
    void testInvalidFormat() {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        reader.handleMessage("invalid,message", storage);

        assertTrue(storage.getAllPatients().isEmpty(),
                "Invalid format should be ignored safely");
    }

    /**
     * Tests corrupted numeric data.
     *
     * Edge case:
     * - Non-numeric values should not break parsing
     */
    @Test
    void testCorruptedData() {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        reader.handleMessage("1,abc,SystolicPressure,xyz", storage);

        assertTrue(storage.getAllPatients().isEmpty(),
                "Corrupted data should not be stored");
    }
}