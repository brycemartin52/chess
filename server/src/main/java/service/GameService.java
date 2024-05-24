package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class GameService {
    UserDAO udao;
    AuthDAO adao;
    GameDAO gdao;

    public GameService(){
        udao = new UserDAO();
        adao = new AuthDAO();
        gdao = new GameDAO();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authData = adao.getAuth(authToken);
        if(authData == null){
            throw new DataAccessException("You are not authorized to create a game.");
        }
//        int i = 0;
//        if(gdao.getGame()){
//            udao.createUser(user);
//            return login(user);
//        }
//        else{
//            throw new DataAccessException(String.format("The user '%s' already exists", user));
//        }
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
