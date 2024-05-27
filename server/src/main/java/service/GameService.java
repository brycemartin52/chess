package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
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

    public GameData joinGame(String authToken, JoinGameData joinRequest) throws DataAccessException {
        AuthData authData = adao.getAuth(authToken);
        if(authData == null){
            throw new DataAccessException("Error: unauthorized");
        }
        GameData game = gdao.getGame(joinRequest.gameID());
        ChessGame.TeamColor reqColor = joinRequest.playerColor();
        if(game == null || reqColor == null){
            throw new DataAccessException("Error: bad request");
        }

        String username = authData.username();
        String curWhiteUser = game.whiteUsername();
        String curBlackUser = game.blackUsername();
        GameData newGameData;

        if(reqColor.equals(ChessGame.TeamColor.WHITE)){
            if(curWhiteUser != null || curBlackUser != null && curBlackUser.equals(username)) {
                throw new DataAccessException("Error: already taken");
            }
            newGameData = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        }
        else{
            if(curBlackUser != null || curWhiteUser != null &&  curWhiteUser.equals(username)) {
                throw new DataAccessException("Error: already taken");
            }
            newGameData = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        gdao.updateGame(newGameData);
        return newGameData;
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
