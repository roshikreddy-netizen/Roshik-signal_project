package data_management;

import com.data_management.DataStorage;
import com.data_management.WebSocketDataReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DataReader streaming behavior.
 *
 * These tests verify lifecycle behavior such as starting and stopping
 * data streams without crashing the system.
 */
class DataReaderTest {

    /**
     * Tests startReading does not crash.
     *
     * Edge case:
     * - Connection attempt without server
     */
    @Test
    void testStartReadingNoServer() {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        assertDoesNotThrow(() -> reader.readData(storage),
                "Reader should handle missing server gracefully");
    }

    /**
     * Tests stopReading safely closes connection.
     *
     * Edge case:
     * - Closing before connection is established
     */
    @Test
    void testStopReadingSafe() {

        WebSocketDataReader reader = new WebSocketDataReader();

        assertDoesNotThrow(reader::stopReading,
                "Stopping reader should not throw errors");
    }
}
