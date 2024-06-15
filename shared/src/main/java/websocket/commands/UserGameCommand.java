package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    protected CommandType commandType;

    private final String authToken;

    private int gameID;

    private ChessMove move;

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public UserGameCommand(CommandType type, String authToken, int gameID, ChessMove move) {
        commandType = type;
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = move;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public ChessMove getMove(){ return this.move;}

    public int getGameID(){return this.gameID;}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
