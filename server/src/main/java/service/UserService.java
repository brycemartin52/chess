package service;

import dataaccess.*;

import model.AuthData;
import model.UserData;
import utils.Encrypt;

import java.util.Objects;

public class UserService {
    SQLUserDAO udao;
    SQLAuthDAO adao;

    public UserService(){
        try{
            udao = new SQLUserDAO();
            adao = new SQLAuthDAO();
        }
        catch(Exception e){
            System.out.println("The database failed to start up");
        }
    }

    public AuthData register(UserData user) throws DataAccessException {
        if(user.username() == null || user.username().isEmpty()){
            throw new DataAccessException("Please enter a username");
        }
        if(user.password() == null){
            throw new DataAccessException("Please enter a password");
        }
        if(udao.getUser(user.username()) == null){
            udao.createUser(user);
            return login(user);
        }
        else{
            throw new DataAccessException(String.format("The user '%s' already exists", user));
        }
    }

    public AuthData login(UserData user) throws DataAccessException {
        if(user == null || user.username() == null || user.password() == null){
            throw new DataAccessException("'null' is an invalid input.");
        }
        if(udao.getUser(user.username()) == null){
            return new AuthData(null, user.username());
        }
        if(!Encrypt.compareHash(user.password(), udao.getPassword(user.username()))){
            return new AuthData(null, user.username());
        }
        if(udao.getUser(user.username()) != null){
            return adao.addAuth(user.username());
        }
        else{
            throw new DataAccessException(String.format("The user '%s' doesn't exist: register to create an account", user.username()));
        }
    }

    public boolean logout(String authToken){
        try{
            return adao.deleteAuth(authToken);
        }
        catch (DataAccessException e){
            System.out.println("Unable to logout");
            return false;
        }
    }

    public void clearUsers() {
        try{
            udao.clear();
        }
        catch (DataAccessException e){
            System.out.println("The database failed to clear.");
        }

    }
}
