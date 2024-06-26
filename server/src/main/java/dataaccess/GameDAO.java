package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameDAO implements GameDAOInterface {

    HashMap<Integer, GameData> gameData;
    ArrayList<String> gameNames;
    int numGames;

    public GameDAO(){
        gameData = new HashMap<>();
        gameNames = new ArrayList<>();
        numGames = 0;
    }

    @Override
    public int createGame(String gameName) {
        numGames++;
        GameData newGame = new GameData(numGames, null, null, gameName, new ChessGame(), false);
        gameData.put(numGames, newGame);
        return numGames;
    }

    @Override
    public GameData getGame(int gameID){
        return gameData.getOrDefault(gameID, null);
    }

    @Override
    public HashSet<GameData> listGames() {
        return new HashSet<>(gameData.values());
    }

    @Override
    public void updateGame(GameData dat) {
        int id = dat.gameID();
        gameData.remove(id);
        gameData.put(id, dat);
    }

    @Override
    public boolean clear() {
        gameData = new HashMap<>();
        return true;
    }
}
