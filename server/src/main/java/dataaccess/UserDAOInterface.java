package dataaccess;

import model.UserData;

public interface UserDAOInterface {

    void createUser(UserData dat) throws DataAccessException ;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

    String getPassword(String username) throws DataAccessException;
}
