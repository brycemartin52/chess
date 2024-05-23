package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAOInterface {

    void createGame(GameData dat);

    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<GameData> listGames();

    void updateGame(GameData dat);

    void clear();
}
