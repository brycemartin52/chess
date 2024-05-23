package dataaccess;

import model.UserData;

public interface UserDAOInterface {

    void createUser(UserData dat) throws DataAccessException;

    UserData getUser(String username);

    void clear();
}
