package service;

import dataaccess.DataAccessException;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    UserDAO udao;
    AuthDAO adao;

    public UserService(){
        udao = new UserDAO();
        adao = new AuthDAO();
    }

    public AuthData register(UserData user) throws DataAccessException {
        if(udao.getUser(user.username()) == null){
            udao.createUser(user);
            return login(user);
        }
        else{
            throw new DataAccessException(String.format("The user '%s' already exists", user));
        }
    }

    public AuthData login(UserData user) throws DataAccessException {
        if(udao.getUser(user.username()) != null){
            return adao.addAuth(user.username());
        }
        else{
            throw new DataAccessException(String.format("The user '%s' doesn't exist: register to create an account", user));
        }
    }

    public boolean logout(String authToken){
        return adao.deleteAuth(authToken);
    }
}
