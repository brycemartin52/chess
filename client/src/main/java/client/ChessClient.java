package client;

import java.util.Arrays;

import com.google.gson.Gson;
import exception.ResponseException;
import ui.ChessBoard;
//import client.websocket.NotificationHandler;
//import client.websocket.WebSocketFacade;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private boolean loggedIn;

//    private final NotificationHandler notificationHandler;
//    private WebSocketFacade ws;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        loggedIn = false;
//        this.notificationHandler = notificationHandler;
    }

    public void displayMenu(){
        if(!loggedIn){
            System.out.println("(R)egister <username> <password> <email>");
            System.out.println("(L)ogin <username> <password>");
            System.out.println("(H)elp");
            System.out.println("(Q)uit");
        }

        else{
            System.out.println("(N)ew Game <Game Name>");
            System.out.println("(A)ll Games");
            System.out.println("(P)lay Game <WHITE/BLACK> <Game ID>");
            System.out.println("(W)atch Game <Game ID>");
            System.out.println("(H)elp");
            System.out.println("Log(O)ut");
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "Help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(loggedIn) {
                return switch (cmd) {
                    case "New", "N" -> createGame(params);
                    case "All", "A" -> listGames();
                    case "Play", "P" -> playGame();
                    case "Watch", "W" -> watchGame(params);
                    case "Help", "H" -> help();
                    case "Logout", "O" -> logout();
                    default -> "Unknown command";
                };
            }
            else{
                return switch (cmd) {
                    case "Register", "R" -> register(params);
                    case "Login", "L" -> login(params);
                    case "Help", "H" -> help();
                    case "Quit", "Q" -> "Quit";
                    default -> "Unknown command";
                };
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            username = params[0];
            String password = params[1];
            String email = params[2];
            if (!email.contains("@")) {
                throw new ResponseException(400, "Must be a valid email");
            }
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            server.register(username, password, email);
            loggedIn = true;
            return String.format("Welcome %s!", username);
        }
        throw new ResponseException(400, "Expected: (R)egister <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            username = params[0];
            String password = params[1];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            server.login(username, password);
            loggedIn = true;
            return String.format("Welcome %s!", username);
        }
        throw new ResponseException(400, "Expected: (L)ogin <username> <password>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
//        ws.leavePetShop(visitorName);
//        ws = null;
        server.logout();
        loggedIn = false;
        return String.format("See you next time, %s.", username);
    }


    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games.values()) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            String gameName = params[0];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            server.createGame(gameName);
            return "Let's get ready to rumble!";
        }
        throw new ResponseException(400, "Expected: (C)reate <Name_of_the_game>");
    }

    public String playGame(Object... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            String team = (String) params[0];
            int gameID = (int) params[1];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            server.playGame(team, gameID);
            ChessBoard.main(new String[0]);
            return "Let's get ready to rumble!";
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
            return "Let's get ready to rumble!";
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