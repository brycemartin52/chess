package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

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
            throw new DataAccessException("Error: unauthorized");
        }
        int id = gdao.createGame(gameName);
        return id;
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws DataAccessException {
        AuthData aData = adao.getAuth(authToken);
        if(aData != null){
            return gdao.listGames();
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public void clearGame(){
        gdao.clear();
    }


}
