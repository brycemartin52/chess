package dataaccess;

import model.GameData;

import java.util.HashMap;

public class SQLGameDAO implements GameDAOInterface {
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public HashMap<Integer, GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData dat) {

    }

    @Override
    public boolean clear() {
        return false;
    }
}
