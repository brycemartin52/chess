package websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


import java.util.HashMap;
import java.util.HashSet;


//need to extend Endpoint for websocket to work properly
@WebSocket
public class WebSocketHandler{
    private HashMap<Integer, HashSet<Session>> sessions;

    public WebSocketHandler(){
        sessions = new HashMap<>();
    }

    @OnWebSocketMessage


}