package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserDAO implements UserDAOInterface{
    static HashMap<String, UserData> userData;

    public UserDAO(){
        userData = new HashMap<>();
    }

    @Override
    public void createUser(UserData data){
        userData.put(data.username(), data);
    }

    @Override
    public UserData getUser(String username) {
        return userData.getOrDefault(username, null);
    }

    @Override
    public void clear() {
        userData = new HashMap<>();
    }

    public String getPassword(String username) {
        UserData user = userData.get(username);
        return user.password();
    }
}
