package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAOInterface{


    @Override
    public AuthData addAuth(String username) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public boolean deleteAuth(String authToken) {
        return false;
    }

    @Override
    public void clear() {

    }
}
