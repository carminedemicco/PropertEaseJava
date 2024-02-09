package main.propertease.server;

import main.propertease.server.proxy.DatabaseConnection;
import main.propertease.server.proxy.DatabaseConnectionProxy;

import java.io.*;
import java.net.Socket;

public class ClientManager {
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

    public void writeLine(String line) {
        outputStream.println(StringUtility.escape(line));
    }

    public String readLine() {
        try {
            final var line = inputStream.readLine();
            if (line == null) {
                return null;
            }
            return line.translateEscapes();
        } catch (Exception e) {
            return null;
        }
    }

    public void close() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatabaseConnection getDatabase() {
        return database;
    }

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
