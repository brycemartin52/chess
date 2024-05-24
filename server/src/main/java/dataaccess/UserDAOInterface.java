package dataaccess;

import model.UserData;

public interface UserDAOInterface {

    void createUser(UserData dat);

    UserData getUser(String username);

    void clear();
}
