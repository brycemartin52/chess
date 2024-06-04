package dataaccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAOInterface{

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData addAuth(String username) throws DataAccessException {
        String authToken = createAuth(username);
        var statement = "INSERT INTO authData (username, authToken) VALUES (?, ?);";
        executeUpdate(statement, username, authToken);
        return new AuthData(authToken, username);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var authToken = rs.getString("authToken");
        return new AuthData(username, authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM authData WHERE authToken=?;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public boolean deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authData where authToken = ?;";
        executeUpdate(statement, authToken);
        return true;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE authData;";
        executeUpdate(statement);
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData (
              `username` int NOT NULL,
              `authToken` varchar(72) NULL,
              PRIMARY KEY (`username`),
              INDEX(authToken)
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
