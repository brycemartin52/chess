package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDAOInterface {

    void createGame(GameData dat);

    GameData getGame(int gameID) throws DataAccessException;

    HashMap<Integer, GameData> listGames();

    void updateGame(GameData dat);

    void clear();
}
