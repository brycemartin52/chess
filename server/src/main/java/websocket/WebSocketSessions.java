package websocket;

import spark.Session;
//import javax.websocket.*;

import java.util.HashMap;


public class WebSocketSessions {
    private HashMap<Integer, HashMap<String, Session>> sessions;

    public WebSocketSessions() {
        this.sessions = new HashMap<>();
    }

    public void addSessionToGame(int gameID, String authToken, Session session) {
        sessions.computeIfAbsent(gameID, k -> new HashMap<>()).put(authToken, session);
    }

    public void removeSessionFromGame(int gameID, String authToken) {
        HashMap<String, Session> gameSessions = getSessionsForGame(gameID);

        if (gameSessions != null) {
            gameSessions.remove(authToken);

            if (gameSessions.isEmpty()) {
                sessions.remove(gameID);
            }
        }
    }

    public HashMap<String, Session> getSessionsForGame(int gameID){
        return sessions.get(gameID);
    }
}
