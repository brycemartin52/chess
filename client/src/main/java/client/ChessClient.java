package client;

import java.util.*;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListGames;
import ui.ChessBoard;
import ui.EscapeSequences;
//import client.websocket.NotificationHandler;
//import client.websocket.WebSocketFacade;

public class ChessClient {
    private String username;
    private String authToken;
    private final ServerFacade server;
    private final String serverUrl;
    private boolean loggedIn;
    private boolean playingGame;

//    private final NotificationHandler notificationHandler;
//    private WebSocketFacade ws;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        loggedIn = false;
        username = null;
        authToken = null;
        playingGame = false;
//        this.notificationHandler = notificationHandler;
    }

    public void displayMenu(){
        if(!loggedIn){
            System.out.println("(R)egister <username> <password> <email>");
            System.out.println("(L)ogin <username> <password>");
            System.out.println("(H)elp");
            System.out.println("(Q)uit");
        }

        if(playingGame){
            System.out.println("(M)ake Move");
            System.out.println("(H)elp");
            System.out.println("(Q)uit");
        }

        else{
            System.out.println("(N)ew <Game Name>");
            System.out.println("(A)ll");
            System.out.println("(P)lay <WHITE/BLACK> <Game ID>");
            System.out.println("(W)atch <Game ID>");
            System.out.println("(H)elp");
            System.out.println("Log(O)ut");
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(input.split(" "), 1, tokens.length);
            if(loggedIn) {
                return switch (cmd) {
                    case "new", "n" -> createGame(params);
                    case "all", "a" -> listGames();
                    case "play", "p" -> playGame(params);
                    case "watch", "w" -> watchGame(params);
                    case "help", "h" -> help();
                    case "logout", "o" -> logout();
                    default -> "Unknown command\n";
                };
            }
            else{
                return switch (cmd) {
                    case "register", "r" -> register(params);
                    case "login", "l" -> login(params);
                    case "help", "h" -> help();
                    case "quit", "q" -> "Quit";
                    default -> "Unknown command";
                };
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            String attemptedUsername = params[0];
            String password = params[1];
            String email = params[2];
            if (!email.contains("@")) {
                return "Must be a valid email";
            }
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            AuthData aData = server.register(attemptedUsername, password, email);
            if(aData == null){
                return String.format("Sorry, but it looks like the username '%s' is already taken.", attemptedUsername);
            }
            username = aData.username();
            authToken = aData.authToken();
            loggedIn = true;
            System.out.println(String.format("Welcome %s!%n", username));
            return help();
        }
        throw new ResponseException(400, "Expected: (R)egister <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            username = params[0];
            String password = params[1];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            AuthData aData = server.login(username, password);
            if(aData == null || aData.authToken() == null){
                username = null;
                return "Please enter a valid username and password";
            }
            username = aData.username();
            authToken = aData.authToken();
            loggedIn = true;
            return String.format("Welcome %s!", username);
        }
        throw new ResponseException(400, "Expected: (L)ogin <username> <password>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
//        ws.leavePetShop(visitorName);
//        ws = null;
        server.logout(authToken);
        String message = String.format("See you next time, %s.", username);
        username = null;
        authToken = null;
        loggedIn = false;
        return message;
    }


    public String listGames() throws ResponseException {
        assertSignedIn();
        ListGames gameSet = server.listGames(authToken);
        if(gameSet == null || gameSet.games().isEmpty()){
            return "There are no games yet. Create one to get started!";
        }
        HashSet<GameData> games = gameSet.games();
        var result = new StringBuilder();
        for (var game : games) {
            result.append(game.gameID()).append(": ").append(game.gameName()).append('\n');
            result.append("White player: ").append(game.whiteUsername()).append("\n");
            result.append("Black player: ").append(game.blackUsername()).append("\n");
        }
        return result.toString();
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            String gameName = params[0];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
           GameData game =  server.createGame(gameName, authToken);
            return "Game Created";
        }
        throw new ResponseException(400, "Expected: (C)reate <Name_of_the_game>");
    }

    public String playGame(Object... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            String color = (String) params[0];
            int gameID = Integer.parseInt((String) params[1]);
            ChessGame.TeamColor team = switch (color){
                case "WHITE", "W", "w" -> ChessGame.TeamColor.WHITE;
                case "BLACK", "B", "b" -> ChessGame.TeamColor.BLACK;
                default -> throw new ResponseException(403, "Unexpected value: " + color);
            };
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            server.playGame(team, gameID, authToken);
            ChessBoard.main(new String[0]);
            playingGame = true;
            return "Joined Game";
        }
        throw new ResponseException(400, "Expected: (P)lay <WHITE or BLACK> <gameID>");
    }

    public String watchGame(Object... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            int gameID = (int) params[0];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            ChessBoard.main(new String[0]);
            playingGame = true;
            return "Joined Game";
        }
        throw new ResponseException(400, "Expected: (P)lay <WHITE or BLACK> <gameID>");
    }

    public String help() {
        if (!loggedIn) {
            return """
                Type...
                    'Register' or 'R': Sign up to play chess
                    'Login' or 'L': Login to view your games
                    'Help' or 'H': Show possible actions
                    'Quit' or 'Q': Exit application
                """;
        }
        return """
                Type...
                    'New' or 'N': Time to start a fresh game
                    'All' or 'A': What are my options?
                    'Play' or 'P': It's about to get crazy
                    'Watch' or 'W': Watch an epic showdown
                    'Help' or 'H': What can I even do?
                    'Logout' or 'L': Back to main menu
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (!loggedIn) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}