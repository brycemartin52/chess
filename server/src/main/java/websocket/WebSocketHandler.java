package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import gson.GsonSerializer;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
//import java.util.HashMap;
import java.util.HashSet;


//need to extend Endpoint for websocket to work properly
@WebSocket
public class WebSocketHandler{
    private WebSocketSessions sessions;
    private final GsonSerializer gsonSerializer;
    private GameService gameService;

    public WebSocketHandler(GameService gService){
        sessions = new WebSocketSessions();
        gsonSerializer = new GsonSerializer();
        gameService = gService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message, String username, GameData game, ChessMove move, ChessGame.TeamColor team) throws IOException {
        UserGameCommand gameCommand = gsonSerializer.serverMessageDeserializer(message);
        try{
            switch (gameCommand.getCommandType()) {
                case CONNECT -> connect(gameCommand.getAuthString(), session, game.gameID(), team);
                case MAKE_MOVE -> makeMove(username, gameCommand.getAuthString(), session, game, move);
                case LEAVE -> leave(username, session, game.gameID());
                case RESIGN -> resign(username, session, game.gameID());
            }
        }
        catch(IOException e){
            var errorMessage = String.format("Calling 'onMessage' with the message '%s' gave the following error: %n%s", message, e.getMessage());
            var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            send(session, gsonSerializer.messageSerializer(joinerNotification));
        }
        catch(DataAccessException e){
            var errorMessage = "Unauthorized to move";
            var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            send(session, gsonSerializer.messageSerializer(joinerNotification));
        }
    }

    public void send(Session session , String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public void broadcast(int gameID, ServerMessage notification, Session exclusionarySession) throws IOException {
        var gameSessions = sessions.getSessionsForGame(gameID);
        HashSet<Session> toRemove = new HashSet<>();

        for (var session : gameSessions) {
            if(session.isOpen() && !session.equals(exclusionarySession)){
                send(session, gsonSerializer.messageSerializer(notification));
            }
            else{toRemove.add(session);}

        }

        for (var session : toRemove){
            sessions.removeSessionFromGame(gameID, session);
        }
    }

    private void connect(String username, Session session, int gameID, ChessGame.TeamColor teamColor) throws IOException {
        String team = switch (teamColor){
            case WHITE -> "White";
            case BLACK -> "Black";
            case null -> "an observer";
        };

        // Send the message to everyone else
        var message = String.format("%s joined the game as %s", username, team);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, notification, session);

        // Add the session, message the joiner
        sessions.addSessionToGame(gameID, session);
        var joinerMessage = String.format("You joined the game as %s", team);
        var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, joinerMessage);
        send(session, gsonSerializer.messageSerializer(joinerNotification));
    }

    private void makeMove(String username, String authString, Session session, GameData game, ChessMove move) throws IOException, DataAccessException {
        gameService.updateGame(authString, game);

        var message = String.format("%s moved from %s to %s", username, move.getStartPosition(), move.getEndPosition());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(game.gameID(), notification, session);
    }

    private void leave(String username, Session session, int gameID) throws IOException {
        sessions.removeSessionFromGame(gameID, session);
        var message = String.format("%s left the game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, notification, session);
    }

    private void resign(String username, Session session, int gameID) throws IOException {
        var message = String.format("%s resigned", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, notification, session);
    }


}