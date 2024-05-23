package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.Objects;

public class UserDAO implements UserDAOInterface{
    Collection<UserData> allUsers;
    Collection<String> allUsernames;

    UserDAO(){}

    @Override
    public void createUser(UserData data) throws DataAccessException{
        String user = data.username();
        if(!allUsernames.contains(user)){
            allUsernames.add(user);
            allUsers.add(data);
        }
        else{
            String errorMessage = String.format("The user '%s' already exists", user);
            throw new DataAccessException(errorMessage);
        }
    }

    @Override
    public UserData getUser(String username) {
        for(UserData user : allUsers){
            if(Objects.equals(user.username(), username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        allUsers.clear();
        allUsernames.clear();
    }
}
