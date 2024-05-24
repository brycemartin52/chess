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
            udao.createUser(user);
            return adao.addAuth(user.username());
        }
        else{
            String errorMessage = String.format("The user '%s' doesn't exist: register to create an account", user);
            throw new DataAccessException(errorMessage);
        }
    }

    public void logout(UserData user) {}
}
