package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import gson.GsonSerializer;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
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
        }
        catch(DataAccessException e){
            System.out.println("Unable to initialize wsHandler DAOs");
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String ugc) {
        UserGameCommand body = gsonSerializer.commandDeserializer(ugc);
        try{
            authToken = body.getAuthString();
            username = adao.getAuth(authToken).username();
            game = gdao.getGame(body.getGameID());
            if (Objects.equals(username, game.whiteUsername())) {team = ChessGame.TeamColor.WHITE;}
            else if (Objects.equals(username, game.blackUsername())) {team = ChessGame.TeamColor.BLACK;}
            else {team = null;}
            try{
                switch (body.getCommandType()) {
                    case CONNECT -> connect(username, session, game.gameID());
                    case MAKE_MOVE -> makeMove(username, session, game, body.getMove());
                    case LEAVE -> leave(username, session, game.gameID());
                    case RESIGN -> resign(username, game.gameID());
                }
            }
            catch(IOException e){
                var errorMessage = String.format("Calling 'onMessage' with the message gave the following error: %n%s", e.getMessage());
                var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "IOException error", errorMessage);
                send(session, gsonSerializer.messageSerializer(joinerNotification));
            }
            catch(InvalidMoveException e){
                String errorMessage = "InvalidMoveException error:" + e.getMessage();
                var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, errorMessage);
                send(session, gsonSerializer.messageSerializer(joinerNotification));
            }
            catch (IllegalArgumentException e){
                String errorMessage = "IllegalArgumentException error:" + e.getMessage();
                var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, errorMessage);
                send(session, gsonSerializer.messageSerializer(joinerNotification));
            }
        }
        catch(DataAccessException e){
            String errorMessage = "DataAccessException error:" + e.getMessage();
            var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, errorMessage);
            send(session, gsonSerializer.messageSerializer(joinerNotification));
        }
        catch(NullPointerException e){
            String errorMessage = "Nullptr error:" + e.getMessage();
            var joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, errorMessage);
            send(session, gsonSerializer.messageSerializer(joinerNotification));
        }
    }

    public void send(Session session , String msg){
        try{
            session.getRemote().sendString(msg);
        }
        catch(IOException e){
            System.out.println("Unable to sent the message:\n" + e.getMessage());
        }

    }

    public void broadcast(int gameID, ServerMessage notification, Session exclusionarySession){
        var gameSessions = sessions.getSessionsForGame(gameID);
        if(gameSessions != null){
            HashSet<Session> toRemove = new HashSet<>();

            for (var session : gameSessions) {
                if(session.isOpen()){
                    if(!session.equals(exclusionarySession)){
                        send(session, gsonSerializer.messageSerializer(notification));
                    }
                }
                else{toRemove.add(session);}
            }

            for (var session : toRemove){
                sessions.removeSessionFromGame(gameID, session);
            }
        }
    }

    private void connect(String username, Session session, int gameID) throws IOException {
        String teamColor = switch (team){
            case WHITE -> "White";
            case BLACK -> "Black";
            case null -> "an observer";
        };

        sessions.addSessionToGame(gameID, session);
        ServerMessage joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        send(session, gsonSerializer.messageSerializer(joinerNotification));

        // Send the message to everyone else
        var message = String.format("%s joined the game as %s", username, teamColor);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, notification, session);
    }

    private void makeMove(String username, Session session, GameData game, ChessMove move) throws IOException, DataAccessException, InvalidMoveException {
        ChessPosition fromPos = move.getStartPosition();
        ChessPosition toPos = move.getEndPosition();
        if (game.finished() || game.game().isInCheckmate(ChessGame.TeamColor.WHITE) || game.game().isInCheckmate(ChessGame.TeamColor.BLACK) ||
                game.game().isInStalemate(ChessGame.TeamColor.WHITE) || game.game().isInStalemate(ChessGame.TeamColor.BLACK)
        ){
            throw new IllegalArgumentException("Game's over folks!");
        }
        if(game.game().getBoard().getPiece(fromPos).getTeamColor() != team){
            throw new IllegalArgumentException("That's not your team! Nice try though.");
        }
        if(game.game().getTeamTurn() != team){
            throw new IllegalArgumentException("That's not your turn! Nice try though.");
        }

        if(!game.game().validMoves(fromPos).contains(move)){
            throw new IllegalArgumentException(String.format("Invalid move: '%s' to '%s'", fromPos, toPos));
        }
        game.game().makeMove(move);
        gdao.updateGame(game);


        ServerMessage joinerNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        broadcast(game.gameID(), joinerNotification, null);

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

    private void resign(String username, int gameID) throws IOException, DataAccessException {
        if(team == null){
            throw new IllegalArgumentException("You're an observer silly, you don't resign.");
        }
        if(game.finished()){
            throw new IllegalArgumentException("Game's already finished ya goon.");
        }

        GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), true);
        gdao.updateGame(updatedGame);
        var message = String.format("%s resigned", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, notification, null);
    }
}