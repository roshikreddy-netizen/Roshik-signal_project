package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Reads real-time data from WebSocket server and stores it.
 *
 * This class adapts streaming input into the existing DataStorage system.
 * Follows Single Responsibility: only handles communication + parsing.
 */
public class WebSocketDataReader implements DataReader {

    private WebSocketClient client;

    @Override
    public void readData(DataStorage dataStorage) {
        try {
            client = new WebSocketClient(new URI("ws://localhost:8080")) {

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to WebSocket server");
                }

                @Override
                public void onMessage(String message) {
                    handleMessage(message, dataStorage);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Connection closed: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("WebSocket error:");
                    ex.printStackTrace();
                }
            };

            client.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopReading() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses incoming message and stores it in DataStorage.
     *
     * Expected format:
     * patientId,timestamp,label,data
     */
    public void handleMessage(String message, DataStorage storage) {

        try {
            String[] parts = message.split(",");

            if (parts.length != 4) {
                System.err.println("Invalid message format: " + message);
                return;
            }

            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String label = parts[2];
            String data = parts[3];

            double value;

            // Handle percentage values like "98%"
            if (data.endsWith("%")) {
                value = Double.parseDouble(data.replace("%", ""));
            } else {
                value = Double.parseDouble(data);
            }

            storage.addPatientData(patientId, value, label, timestamp);

        } catch (Exception e) {
            System.err.println("Error parsing message: " + message);
            e.printStackTrace();
        }
    }
}