package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import gson.GsonSerializer;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {
    private WebSocketSessions sessions;
    private final GsonSerializer gsonSerializer;
    private SQLAuthDAO adao;
    private SQLGameDAO gdao;
    private SQLUserDAO udao;

    String authToken;
    String username;
    GameData game;
    ChessGame.TeamColor team;

    public WebSocketHandler(){
        sessions = new WebSocketSessions();
        gsonSerializer = new GsonSerializer();
        try{
            adao = new SQLAuthDAO();
            gdao = new SQLGameDAO();
            udao = new SQLUserDAO();
        }
        catch(DataAccessException e){
            System.out.println("Unable to initialize wsHandler DAOs");
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String ugc) throws IOException {
        UserGameCommand body = gsonSerializer.commandDeserializer(ugc);
        try{
            authToken = body.getAuthString();
            username = adao.getAuth(authToken).username();
            game = gdao.getGame(body.getGameID());
            if (Objects.equals(username, game.whiteUsername())) {team = ChessGame.TeamColor.WHITE;}
            else if (Objects.equals(username, game.blackUsername())) {team = ChessGame.TeamColor.BLACK;}
            else {team = null;}
        }
        catch(DataAccessException e){
            System.out.println("There was a problem initializing the onMessage\n"+e.getMessage());
        }

        try{
            switch (body.getCommandType()) {
                case CONNECT -> connect(username, session, game.gameID(), team);
                case MAKE_MOVE -> makeMove(username, session, game, body.getMove());
                case LEAVE -> leave(username, session, game.gameID());
                case RESIGN -> resign(username, session, game.gameID());
            }
        }
        catch(IOException e){
            var errorMessage = String.format("Calling 'onMessage' with the message gave the following error: %n%s", e.getMessage());
            var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            send(session, gsonSerializer.messageSerializer(joinerNotification));
        }
        catch(DataAccessException e){
            String errorMessage = e.getMessage();

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

    private void makeMove(String username, Session session, GameData game, ChessMove move) throws IOException, DataAccessException {
        gdao.updateGame(game);

        var message = String.format("%s moved from %s to %s", username, move.getStartPosition(), move.getEndPosition());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(game.gameID(), notification, session);
    }

    private void leave(String username, Session session, int gameID) throws IOException, DataAccessException {
        if(team == ChessGame.TeamColor.WHITE){
            GameData updatedGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game(), game.finished());
            gdao.updateGame(updatedGame);
        }
        else if (team == ChessGame.TeamColor.BLACK){
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game(), game.finished());
            gdao.updateGame(updatedGame);
        }
        //Update the game here
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