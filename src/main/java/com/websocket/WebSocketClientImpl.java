package com.websocket;

import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * WebSocket client that receives real-time patient data.
 *
 * 
 * - Uses event-driven design to handle streaming data
 * - Keeps network logic separate from alert logic
 * - Improves modularity and testability
 */
public class WebSocketClientImpl extends WebSocketClient {

    private DataStorage dataStorage;

    /**
     * Creates a WebSocket client linked to data storage.
     *
     *
     * - Dependency injection is used to reduce tight coupling
     */
    public WebSocketClientImpl(String serverUri, DataStorage dataStorage) {
        super(URI.create(serverUri));
        this.dataStorage = dataStorage;
    }

    /**
     * Called when connection opens.
     *
     * 
     * - Confirms successful connection setup
     */
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("WebSocket connected");
    }

    /**
     * Handles incoming messages from server.
     *
     * format assumption:
     * patientId,timestamp,label,value
     *
     * Reason:
     * - Parses stream into structured data
     * - Sends data to DataStorage for persistence
     */
    @Override
    public void onMessage(String message) {

        try {
            String[] parts = message.split(",");

            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String label = parts[2];
            double value = Double.parseDouble(parts[3].replace("%", ""));

            dataStorage.addPatientData(patientId, value, label, timestamp);

        } catch (Exception e) {

            /**
             * Reason:
             * - Prevents system crash from bad network data
             * - Ensures resilience in real-time systems
             */
            System.err.println("Invalid message received: " + message);
        }
    }

    /**
     * Handles connection closure.
     *
     *
     * - Logs disconnect events for debugging and monitoring
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + reason);
    }

    /**
     * Handles runtime errors in WebSocket communication.
     *
     * 
     * - Ensures failures are visible and diagnosable
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }
}