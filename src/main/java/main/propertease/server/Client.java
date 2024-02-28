package main.propertease.server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple Client wrapper for sending and receiving JSON data.
 */
public class Client {
    /**
     * Creates a new client.
     *
     * @param host The host to connect to.
     * @param port The port to connect to.
     * @throws RuntimeException If an error occurs during connection.
     */
    public Client(String host, int port) {
        try {
            /*
             * Creates a new Socket and connects to the host specified by *host* and *port*
             */
            socket = new Socket(host, port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a JSON object to the server.
     *
     * @param data The data to send.
     */
    public void send(JSONObject data) {
        try {
            final var writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(StringUtility.escape(data.toString()));
            writer.flush();
        } catch (Exception ignored) {
        }
    }

    /**
     * Receives a JSON object from the server.
     *
     * @return The received JSON object if successful, <b>null</b> otherwise.
     */
    public JSONObject receive() {
        try {
            final var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final var line = reader.readLine();
            if (line == null) {
                return null;
            }
            return new JSONObject(line.translateEscapes());
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Sends a JSON object to the server and awaits a response.
     *
     * @param data The data to send.
     * @return The received JSON object.
     */
    public JSONObject exchange(JSONObject data) {
        send(data);
        return receive();
    }

    /**
     * Closes the client, releasing all resources.
     */
    public void close() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (Exception ignored) {
        }
    }

    private final Socket socket;
}
