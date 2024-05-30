package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;

public class SQLGameDAO implements GameDAOInterface {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public HashMap<Integer, GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData dat) {

    }

    @Override
    public boolean clear() {
        return false;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(128) NULL,
              `blackUsername` varchar(128) NULL,
              `gameName` varchar(128) NULL,
              `game` longtext NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName),
              INDEX(game)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
