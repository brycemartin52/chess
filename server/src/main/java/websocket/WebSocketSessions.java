package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;


public class WebSocketSessions {
    private HashMap<Integer, HashSet<Session>> sessions;

    public WebSocketSessions() {
        this.sessions = new HashMap<>();
    }

    public void addSessionToGame(int gameID, Session session) {
        sessions.computeIfAbsent(gameID, k -> new HashSet<>()).add(session);
    }

    public void removeSessionFromGame(int gameID, Session session) {
        HashSet<Session> gameSessions = getSessionsForGame(gameID);

        if (gameSessions != null) {
            gameSessions.remove(session);

            if (gameSessions.isEmpty()) {
                sessions.remove(gameID);
            }
        }
    }

    public HashSet<Session> getSessionsForGame(int gameID){
        return sessions.get(gameID);
    }
}
