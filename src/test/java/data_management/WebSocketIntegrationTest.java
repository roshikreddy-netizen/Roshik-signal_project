package data_management;

import com.data_management.DataStorage;
import com.data_management.WebSocketDataReader;
import com.alerts.AlertGenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for real-time system.
 *
 * Verifies that:
 * WebSocket -> DataStorage -> AlertGenerator pipeline works correctly.
 */
class WebSocketIntegrationTest {

    /**
     * Tests full pipeline from message ingestion to alert generation.
     *
     * Edge case:
     * - Ensures combined condition triggers alert correctly
     */
    @Test
    void testFullPipeline() throws InterruptedException {

        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader();

        // simulate incoming data
        reader.handleMessage("1,1000,SystolicPressure,80", storage);
        reader.handleMessage("1,1001,Saturation,85", storage);

        AlertGenerator generator = new AlertGenerator(storage);
        generator.evaluateData();

        assertTrue(
                generator.getEmittedAlerts().stream()
                        .anyMatch(a -> a.getCondition().contains("HYPOTENSIVE_HYPOXEMIA")),
                "Combined condition should trigger alert"
        );
    }
}
