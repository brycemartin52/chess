package gson;

import com.google.gson.Gson;

import chess.ChessGame;
import model.UserData;

public class gsonSerializer {
    Gson serializer;

    public gsonSerializer(){
        serializer = new Gson();
    }

    public String gameSerializer(ChessGame game){
        return serializer.toJson(game);
    }

    public String userSerializer(UserData user){
        return serializer.toJson(user);
    }

    public ChessGame gameDeserializer(String game){
        return serializer.fromJson(game, ChessGame.class);
    }

    public UserData userDeserializer(String user){
        return serializer.fromJson(user, UserData.class);
    }


}
