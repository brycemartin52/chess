package dataaccess;

import model.GameData;

import java.util.HashMap;

public class GameDAO implements GameDAOInterface{

    HashMap<Integer, GameData> gameData;
    int numGames;

    public GameDAO(){}

    @Override
    public void createGame(GameData dat) {
        numGames++;
        int id = dat.gameID();
        gameData.put(id, dat);
    }

    @Override
    public GameData getGame(int gameID){
        return gameData.getOrDefault(gameID, null);
    }

    @Override
    public HashMap<Integer, GameData> listGames() {
        return gameData;
    }

    @Override
    public void updateGame(GameData dat) {
        int id = dat.gameID();
        gameData.put(id, dat);
    }

    @Override
    public void clear() {
        gameData = new HashMap<>();
    }
}
