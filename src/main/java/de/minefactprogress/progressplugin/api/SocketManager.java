package de.minefactprogress.progressplugin.api;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.utils.Logger;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketManager {
    private final Socket socket;

    public SocketManager(String ip) {
        Socket s;
        try {
            s = IO.socket(ip, IO.Options.builder().build());
            s.connect();
        } catch (URISyntaxException e) {
            s = null;
            Logger.error("Error occurred while connection to socket: " + ip);
        }
        this.socket = s;
        joinRoom("motd");
    }

    public void sendMessage(String event, Object... message) {
        socket.emit(event, message);
    }

    public void joinRoom(String room) {
        JsonObject roomJson = new JsonObject();
        roomJson.addProperty("room", room);

        JsonObject json = new JsonObject();
        json.add("data", roomJson);
        sendMessage("join", json);
    }

    public void listenEvent(String event) {
        socket.on(event, objects -> {
            System.out.println(objects[0]);
        });
    }

    public void disconnect() {
        this.socket.disconnect();
    }
}
