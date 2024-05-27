package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAOInterface {

    int createGame(String gameName);

    GameData getGame(int gameID) throws DataAccessException;

    HashMap<Integer, GameData> listGames();

    void updateGame(GameData dat);

    boolean clear();
}
