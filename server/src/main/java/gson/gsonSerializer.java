package gson;

import com.google.gson.Gson;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.ErrorMessage;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;

import java.util.HashMap;

public class gsonSerializer {
    Gson serializer;

    public gsonSerializer(){
        serializer = new Gson();
    }

    public String gameSerializer(GameData game){
        return serializer.toJson(game);
    }
    public String gamesSerializer(HashMap<Integer, GameData> games){
        return serializer.toJson(games);
    }
    public String userSerializer(UserData user){
        return serializer.toJson(user);
    }
    public String authSerializer(AuthData auth){
        return serializer.toJson(auth);
    }
    public String errSerializer(ErrorMessage error) { return serializer.toJson(error);}

    public GameData gameDeserializer(String game){
        return serializer.fromJson(game, GameData.class);
    }
    public UserData userDeserializer(String user){
        return serializer.fromJson(user, UserData.class);
    }
    public AuthData authDeserializer(String auth) { return serializer.fromJson(auth, AuthData.class);}
    public ErrorMessage errDeserializer(String error) { return serializer.fromJson(error, ErrorMessage.class);}
    public JoinGameData joinDeserializer(String req) { return serializer.fromJson(req, JoinGameData.class); }
}
