package main.propertease.server;

import main.propertease.server.proxy.DatabaseConnection;
import main.propertease.server.proxy.DatabaseConnectionProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Utility class to manage a client's connection to the server.
 */
public class ClientManager {
    /**
     * Creates a new client manager, also creates a proxy for the database connection.
     *
     * @param socket The socket to manage.
     * @throws RuntimeException If an error occurs during connection.
     */
    public ClientManager(Socket socket) {
        this.socket = socket;
        this.database = new DatabaseConnectionProxy();
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an escaped, JSON string to the <b>outputStream</b>
     *
     * @param line The unescaped JSON string to send.
     */
    public void writeLine(String line) {
        outputStream.println(StringUtility.escape(line));
    }

    /**
     * Reads a line from the <b>inputStream</b> and unescapes it.
     *
     * @return The escaped line read from the <b>inputStream</b> if successful, <b>null</b> otherwise.
     */
    public String readLine() {
        try {
            final var line = inputStream.readLine();
            if (line == null) {
                return null;
            }
            return line.translateEscapes();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Closes the input and output streams and the socket.
     */
    public void close() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Gets the database connection proxy.
     *
     * @return The database connection proxy.
     */
    public DatabaseConnection getDatabase() {
        return database;
    }

    /**
     * Creates an error message with the given message.
     *
     * @param message The message to include in the error message.
     * @return The error message encoded as an escaped JSON string
     */
    public String makeErrorMessage(String message) {
        final var template = """
            {"type":"error","message":"%s"}
        """;
        return String.format(template, StringUtility.escape(message));
    }

    private final DatabaseConnection database;
    private final Socket socket;
    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
}
