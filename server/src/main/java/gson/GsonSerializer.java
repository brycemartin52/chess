package gson;

import chess.ChessGame;
import com.google.gson.Gson;

import dataaccess.ErrorMessage;
import model.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class GsonSerializer {
    Gson serializer;

    public GsonSerializer(){
        serializer = new Gson();
    }

    public String gameSerializer(GameData game){
        return serializer.toJson(game);
    }
    public String chessGameSerializer(ChessGame game) {return serializer.toJson(game);}
    public String authSerializer(AuthData auth){
        return serializer.toJson(auth);
    }
    public String errSerializer(ErrorMessage error) { return serializer.toJson(error);}
    public String messageSerializer(ServerMessage message) { return serializer.toJson(message);}
    public String wsSerialixer(WebSocketData data) { return serializer.toJson(data);}

    public UserData userDeserializer(String user){
        return serializer.fromJson(user, UserData.class);
    }
    public JoinGameData joinDeserializer(String req) { return serializer.fromJson(req, JoinGameData.class); }
    public GameData gameDeserializer(String game){
        return serializer.fromJson(game, GameData.class);
    }
    public ChessGame chessGameDeserializer(String game) {return serializer.fromJson(game, ChessGame.class);}
    public UserGameCommand serverMessageDeserializer(String message) {return serializer.fromJson(message, UserGameCommand.class);}
    public WebSocketData wsDeserializer(String message) {return serializer.fromJson(message, WebSocketData.class);}
}
