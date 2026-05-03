package data_management;

import com.data_management.DataStorage;
import com.data_management.WebSocketDataReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests robustness of WebSocket client.
 *
 * Focuses on handling unexpected failures without crashing.
 */
class WebSocketErrorHandlingTest {

    /**
     * Tests null message handling.
     *
     * Edge case:
     * - Null input should not crash system
     */
    @Test
    void testNullMessage() {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        assertDoesNotThrow(() ->
                reader.handleMessage(null, storage),
                "Null messages should be handled safely"
        );
    }

    /**
     * Tests extremely large message input.
     *
     * Edge case:
     * - Prevent overflow or parsing crash
     */
    @Test
    void testLargeMessage() {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        String largeMessage = "1,1000,SystolicPressure," + "9".repeat(1000);

        assertDoesNotThrow(() ->
                reader.handleMessage(largeMessage, storage),
                "Large messages should not crash system"
        );
    }
}