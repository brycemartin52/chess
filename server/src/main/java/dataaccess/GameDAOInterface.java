package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAOInterface {

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    HashMap<Integer, GameData> listGames() throws DataAccessException;

    void updateGame(GameData dat) throws DataAccessException;

    boolean clear() throws DataAccessException;
}
