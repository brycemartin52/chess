package dataaccess;

import model.AuthData;

import java.util.UUID;

public interface AuthDAOInterface {
    default String createAuth(String username){
        return UUID.randomUUID().toString();
    }

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void clear();
}
