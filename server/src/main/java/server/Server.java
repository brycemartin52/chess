package server;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.ErrorMessage;
import gson.gsonSerializer;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.HashMap;

public class Server {
    gsonSerializer gSerializer;
    GameService gService;
    UserService uService;
    AuthService aService;

    public Server(){
        gSerializer = new gsonSerializer();

        gService = new GameService();
        uService = new UserService();
        aService = new AuthService();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGameHandler);
        Spark.post("/game", this::createGameHandler);
//        Spark.put("/game", this::joinGameHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }


    private Object clearHandler(Request request, Response response) {
//        try {
            gService.clearGame();
            uService.clearUsers();
            aService.clearAuths();
            response.status(200);
            return "{}";
//        }

//        catch () {
//            String res = "{\"message\": \"Error: (description of error)\"}";
//            response.status(500);
//            return res;
//        }

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
            String res = "gSerializer.errSerializer(e)";
            response.status(403);
            return res;
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
            HashMap<Integer, GameData> games = gService.listGames(header);
            String allGames = gSerializer.gamesSerializer(games);
            response.status(200);
            return allGames;
        }
        catch(DataAccessException e){
            ErrorMessage error = new ErrorMessage("Error: already taken");
            String message = gSerializer.errSerializer(error);
            response.status(401);
            return message;
        }
    }

    private Object createGameHandler(Request request, Response response) {
        String header = request.headers("authorization");
        String body = request.body();
        try{
            int code = gService.createGame(header, body);
            ChessGame newGame = new ChessGame();
            GameData gData = new GameData(code, null, null, body, newGame);
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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
