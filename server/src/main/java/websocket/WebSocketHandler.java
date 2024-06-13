package websocket;

import com.google.gson.Gson;
import gson.GsonSerializer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


//need to extend Endpoint for websocket to work properly
@WebSocket
public class WebSocketHandler{
    private WebSocketSessions sessions;
    private GsonSerializer gsonSerializer;

    public WebSocketHandler(){
        sessions = new WebSocketSessions();
        gsonSerializer = new GsonSerializer();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message, String username, int gameID) throws IOException {
        UserGameCommand gameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()) {
            case CONNECT -> connect(gameCommand.getAuthString(), session, gameID);
            case MAKE_MOVE -> makeMove(gameCommand.getAuthString(), session, gameID);
            case LEAVE -> leave(username, session, gameID);
            case RESIGN -> resign(username, gameID);
        }
    }

    public void send(Session session , String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public void broadcast(int gameID, ServerMessage notification) throws IOException {
        var gameSessions = sessions.getSessionsForGame(gameID);
        for (var session : gameSessions) {
            send(session, notification.toString());
        }
    }

    private void connect(String authString, Session session, int gameID) {

    }

    private void makeMove(String authString, Session session, int gameID) {

    }

    private void leave(String username, Session session, int gameID) throws IOException {
        sessions.removeSessionFromGame(gameID, session);
        var message = String.format("%s left the game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, notification);
    }

    private void resign(String username, int gameID) throws IOException {
        var message = String.format("%s resigned", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, notification);
    }


}