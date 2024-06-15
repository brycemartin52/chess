package model;

import chess.ChessGame;
import chess.ChessMove;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

public record WebSocketData (UserGameCommand.CommandType message, String username, GameData game, ChessMove move, ChessGame.TeamColor team){
}
