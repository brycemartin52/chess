package dataaccess;


import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class GameDAO implements GameDAOInterface{

    ArrayList<GameData> allGameData;
    ArrayList<Integer> allGameIDs;

    public GameDAO(){
    }

    @Override
    public void createGame(GameData dat) {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        if(allGameIDs.contains(gameID)){
            for(GameData dat : allGameData){
                if(Objects.equals(dat.gameID(), gameID)){
                    return dat;
                }
            }
        }
        else{
            throw new DataAccessException("This game already exists");
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return allGameData;
    }

    @Override
    public void updateGame(GameData dat) {
        int id = dat.gameID();
        for(GameData oldDat : allGameData){
            if(Objects.equals(oldDat.gameID(), id)){
                allGameData.remove(oldDat);
                allGameData.add(dat);
            }
        }
    }

    @Override
    public void clear() {
        allGameData = new ArrayList<>();
        allGameIDs = new ArrayList<>();
    }
}
