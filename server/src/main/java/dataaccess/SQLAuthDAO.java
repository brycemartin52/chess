package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAOInterface{

    public SQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS authData (
              `username` varchar(128) NOT NULL,
              `authToken` varchar(72) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        SharedSQLMethods.configureDatabase(createStatements);
    }

    @Override
    public AuthData addAuth(String username) throws DataAccessException {
        String authToken = createAuth(username);
        var newstatement = "INSERT INTO authData (username, authToken) VALUES (?, ?);";
        SharedSQLMethods.executeUpdate(newstatement, username, authToken);
        return new AuthData(authToken, username);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var authToken = rs.getString("authToken");
        return new AuthData(authToken, username);
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

    public AuthData getAuth(UserData user) throws DataAccessException {
        String username = user.username();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM authData WHERE username=?;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
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
        AuthData authData = getAuth(authToken);
        if(authData == null){
            return false;
        }
        var statement = "DELETE FROM authData where authToken = ?;";
        SharedSQLMethods.executeUpdate(statement, authToken);
        return true;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE authData;";
        SharedSQLMethods.executeUpdate(statement);
    }
}
