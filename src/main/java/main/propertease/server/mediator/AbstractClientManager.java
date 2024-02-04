package main.propertease.server.mediator;

import main.propertease.server.StringUtility;

import java.io.*;
import java.net.Socket;

public abstract class AbstractClientManager {
    public AbstractClientManager(ServerMediator serverMediator, Socket socket) {
        this.serverMediator = serverMediator;
        this.socket = socket;
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void broadcast(String message);

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
            e.printStackTrace();
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

    public String makeErrorMessage(String message) {
        final var template = """
          {"type":"error","message":"%s"}
        """;
        return String.format(template, StringUtility.escape(message));
    }

    protected final ServerMediator serverMediator;
    private final Socket socket;
    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
}
