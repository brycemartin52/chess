package gson;

import com.google.gson.Gson;

import dataaccess.ErrorMessage;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;

import java.util.Collection;

public class gsonSerializer {
    Gson serializer;

    public gsonSerializer(){
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
}
