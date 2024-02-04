package main.propertease.server;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class Client {
    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send(JSONObject data) {
        try {
            final var writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(StringUtility.escape(data.toString()));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject receive() {
        try {
            final var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final var line = reader.readLine();
            if (line == null) {
                return null;
            }
            return new JSONObject(line.translateEscapes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject exchange(JSONObject data) {
        send(data);
        return receive();
    }

    public void close() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Socket socket;
}
