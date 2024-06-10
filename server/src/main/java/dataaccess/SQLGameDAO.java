package dataaccess;

import chess.ChessGame;
import gson.GsonSerializer;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAOInterface {
    GsonSerializer gSerializer;

    public SQLGameDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS gameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(128) NULL,
              `blackUsername` varchar(128) NULL,
              `gameName` varchar(128) NOT NULL,
              `game` longtext NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        SharedSQLMethods.configureDatabase(createStatements);
        gSerializer = new GsonSerializer();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO gameData (gameName, game) VALUES (?, ?);";
        ChessGame newGame = new ChessGame();
        String gameJson = gSerializer.chessGameSerializer(newGame);
        return executeUpdate(statement, gameName, gameJson);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var game = gSerializer.chessGameDeserializer(rs.getString("game"));
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, game, whiteUsername, blackUsername, gameName FROM gameData WHERE gameID=?;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        var result = new HashSet<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameData;";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("gameID");
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public void updateGame(GameData newGame) throws DataAccessException {
        var statement = "UPDATE gameData SET game = ?, whiteUsername = ?, blackUsername = ? WHERE gameID=?;";
        String gameJson = gSerializer.chessGameSerializer(newGame.game());
        executeUpdate(statement, gameJson, newGame.whiteUsername(), newGame.blackUsername(), newGame.gameID());
    }

    @Override
    public boolean clear() throws DataAccessException {
        var statement = "TRUNCATE gameData;";
        executeUpdate(statement);
        return true;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

}
