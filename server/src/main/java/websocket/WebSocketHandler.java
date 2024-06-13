package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;


//need to extend Endpoint for websocket to work properly
@WebSocket
public class WebSocketHandler{
    private WebSocketSessions sessions;

    public WebSocketHandler(){
        sessions = new WebSocketSessions();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand gameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()) {
            case CONNECT -> connect(gameCommand.getAuthString(), session);
            case MAKE_MOVE -> makeMove(gameCommand.getAuthString(), session);
            case LEAVE -> leave(gameCommand.getAuthString(), session);
            case RESIGN -> resign(gameCommand.getAuthString(), session);
        }
    }

    private void connect(String authString, Session session) {
    }

    private void makeMove(String authString, Session session) {
    }

    private void leave(String authString, Session session) {
    }

    private void resign(String authString, Session session) {
    }


}