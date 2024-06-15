package websocket.messages;

import model.GameData;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String errorMessage;
    GameData game;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    // Notification Message
    public ServerMessage(ServerMessageType type, String message) {
        this.serverMessageType = type;
        this.message = message;
    }

    // Error Message
    public ServerMessage(ServerMessageType type, String message, String errorMessage) {
        this.serverMessageType = type;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    // Load Game Message
    public ServerMessage(ServerMessageType type, GameData gameData) {
        this.serverMessageType = type;
        this.game = gameData;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage() {
        return this.message;
    }

    public String getErrorMessage(){return this.errorMessage;}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
