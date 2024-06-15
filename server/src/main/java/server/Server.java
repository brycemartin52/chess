package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorMessage;
import gson.GsonSerializer;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import websocket.WebSocketHandler;
//import websocket.WebSocketHandler;

import java.util.HashSet;
import java.util.Map;

public class Server {
    GsonSerializer gSerializer;
    GameService gService;
    UserService uService;
    AuthService aService;
//    WebSocketHandler wService;

    public Server(){
        try{
        gSerializer = new GsonSerializer();

        gService = new GameService();
        uService = new UserService();
        aService = new AuthService();
        }
        catch(Exception e){
        System.out.println("The Server failed to start up");
        }
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", new WebSocketHandler());

        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGameHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }


    private Object clearHandler(Request request, Response response) {
            gService.clearGame();
            uService.clearUsers();
            aService.clearAuths();
            response.status(200);
            return "{}";
    }

    private Object registerHandler(Request request, Response response){
        String body = request.body();
        UserData req = gSerializer.userDeserializer(body);

        try{
            AuthData aData = uService.register(req);
            String res = gSerializer.authSerializer(aData);
            response.status(200);
            return res;
        }

        catch(DataAccessException e){
            if(e.getMessage().equals("Please enter a password") || e.getMessage().equals("Please enter a username")){
                ErrorMessage error = new ErrorMessage("Error: bad request");
                String message = gSerializer.errSerializer(error);
                response.status(400);
                return message;
            }
            ErrorMessage error = new ErrorMessage("Error: already taken");
            String message = gSerializer.errSerializer(error);
            response.status(403);
            return message;
        }
    }

    private Object loginHandler(Request request, Response response) {
        String body = request.body();
        UserData req = gSerializer.userDeserializer(body);

        try{
            AuthData aData = uService.login(req);
            if(aData.authToken() == null){
                ErrorMessage error = new ErrorMessage("Error: unauthorized");
                String message = gSerializer.errSerializer(error);
                response.status(401);
                return message;
            }


            else{
                String res = gSerializer.authSerializer(aData);
                response.status(200);
                return res;
            }
        }

        catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: already logged in");
            String message = gSerializer.errSerializer(error);
            response.status(403);
            return message;
        }
    }

    private Object logoutHandler(Request request, Response response) {
        String header = request.headers("authorization");
        boolean success = uService.logout(header);

        if(success){
            String res = "{}";
            response.status(200);
            return res;
        }

        else{
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            String message = gSerializer.errSerializer(error);
            response.status(401);
            return message;
        }
    }

    private Object listGameHandler(Request request, Response response) {
        String header = request.headers("authorization");
        try{
            HashSet<GameData> games = gService.listGames(header);
//            Collection<GameData> list = games.values();
            response.status(200);
            return new Gson().toJson(Map.of("games", games));
        }
        catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: unauthorized");
            String message = gSerializer.errSerializer(error);
            response.status(401);
            return message;
        }
    }

    private Object createGameHandler(Request request, Response response) {
        String header = request.headers("authorization");
        String body = gSerializer.gameDeserializer(request.body()).gameName();
        try{
            int code = gService.createGame(header, body);
            ChessGame newGame = new ChessGame();
            GameData gData = new GameData(code, null, null, body, newGame, false);
            String game = gSerializer.gameSerializer(gData);
            response.status(200);
            return game;
        }
        catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: already taken");
            String message = gSerializer.errSerializer(error);
            response.status(401);
            return message;
        }
    }

    private Object joinGameHandler(Request request, Response response) {
        String header = request.headers("authorization");
        JoinGameData body = gSerializer.joinDeserializer(request.body());
        try{
            gService.joinGame(header, body);
            response.status(200);
            return "{}";
        }
        catch(DataAccessException e){
            if(e.getMessage().equals("Error: unauthorized")){
                ErrorMessage error = new ErrorMessage("Error: unauthorized");
                String message = gSerializer.errSerializer(error);
                response.status(401);
                return message;
            }
            if(e.getMessage().equals("Error: bad request")){
                ErrorMessage error = new ErrorMessage("Error: unauthorized");
                String message = gSerializer.errSerializer(error);
                response.status(400);
                return message;
            }
            ErrorMessage error = new ErrorMessage("Error: already taken");
            String message = gSerializer.errSerializer(error);
            response.status(403);
            return message;
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
