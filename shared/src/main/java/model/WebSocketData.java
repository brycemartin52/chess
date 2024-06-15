package model;

import chess.ChessGame;
import chess.ChessMove;
import org.eclipse.jetty.websocket.api.Session;

public record WebSocketData (String message, String username, GameData game, ChessMove move, ChessGame.TeamColor team){
}
