package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Implements an output strategy that sends patient data over TCP.
 * This class sets up a TCP server on a port and transmits
 * patient measurements to the connected client.
 * 
 * @author Roshik
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Creates a TCP server on a port and waits for a client connection.
     * Once a client connects, a PrintWriter is initialized to send messages.
     * 
     * @param port TCP port number to listen on
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a patient data message to the connected TCP client.
     * The message is formatted as "patientId,timestamp,label,data".
     * If no client is connected, the message is silently ignored.
     * 
     * @param patientId ID of the patient
     * @param timestamp timestamp of the measurement in milliseconds
     * @param label type of measurement
     * @param data measurement value as a string
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}