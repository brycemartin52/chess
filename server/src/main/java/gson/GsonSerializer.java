package gson;

import com.google.gson.Gson;

import dataaccess.ErrorMessage;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;

public class GsonSerializer {
    Gson serializer;

    public GsonSerializer(){
        serializer = new Gson();
    }

    public String gameSerializer(GameData game){
        return serializer.toJson(game);
    }
    public String authSerializer(AuthData auth){
        return serializer.toJson(auth);
    }
    public String errSerializer(ErrorMessage error) { return serializer.toJson(error);}

    public UserData userDeserializer(String user){
        return serializer.fromJson(user, UserData.class);
    }
    public JoinGameData joinDeserializer(String req) { return serializer.fromJson(req, JoinGameData.class); }
    public GameData gameDeserializer(String game){
        return serializer.fromJson(game, GameData.class);
    }
}
