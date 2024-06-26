package dataaccess;

import model.UserData;
import utils.Encrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAOInterface{

    public SQLUserDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS userData (
              `username` varchar(128) NOT NULL,
              `password` varchar(128) NOT NULL,
              `email` varchar(128) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        SharedSQLMethods.configureDatabase(createStatements);
    }

    @Override
    public void createUser(UserData dat) throws DataAccessException {
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?);";
        String passwordHash = Encrypt.getHash(dat.password());
        SharedSQLMethods.executeUpdate(statement, dat.username(), passwordHash, dat.email());
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userData WHERE username=?;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE userData;";
        SharedSQLMethods.executeUpdate(statement);
    }

    @Override
    public String getPassword(String username) throws DataAccessException {
        UserData userData = getUser(username);
        return userData.password();
    }
}
