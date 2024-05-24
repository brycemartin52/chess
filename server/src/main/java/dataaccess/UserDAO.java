package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserDAO implements UserDAOInterface{
    static HashMap<String, UserData> userData;

    public UserDAO(){}

    @Override
    public void createUser(UserData data){
        String username = data.username();
        if(!userData.containsKey(username)){
            userData.put(username, data);
        }
    }

    @Override
    public UserData getUser(String username) {
        return userData.getOrDefault(username, null);
    }

    @Override
    public void clear() {
        userData = new HashMap<>();
    }
}
