package dataaccess;

import model.AuthData;

import java.util.UUID;

public interface AuthDAOInterface {
    default String createAuth(String username){
        return UUID.randomUUID().toString();
    }

    AuthData addAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    boolean deleteAuth(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;
}
