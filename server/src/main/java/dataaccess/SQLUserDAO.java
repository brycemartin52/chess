package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAOInterface{
    @Override
    public void createUser(UserData dat) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getPassword(String username) {
        return "";
    }
}
