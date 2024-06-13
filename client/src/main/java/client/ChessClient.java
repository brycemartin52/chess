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
    private boolean inGame;
    private ChessGame currentGame;

//    private final NotificationHandler notificationHandler;
//    private WebSocketFacade ws;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        loggedIn = false;
        username = null;
        authToken = null;
        inGame = false;
//        this.notificationHandler = notificationHandler;
    }

    public void displayMenu(){
        if(!loggedIn){
            System.out.println("(R)egister <username> <password> <email>");
            System.out.println("(L)ogin <username> <password>");
            System.out.println("(H)elp");
            System.out.println("(Q)uit");
        }

        else if(inGame){
            System.out.println("(R)edraw");
            System.out.println("(Hi)ghlight <piecePos>");
            System.out.println("(L)eave");
            System.out.println("Resign/(Q)");
            System.out.println("(H)elp");
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

    public String eval(String input){
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(input.split(" "), 1, tokens.length);
            if(loggedIn) {
                if(!inGame) {
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
                        case "help", "h" -> help();
                        case "redraw", "r" -> ChessBoard.printBoard(currentGame);
                        case "leave", "l" -> listGames();
                        case "resign", "q" -> playGame();
                        case "move", "m" -> makeMove(params);
                        case "highlight", "hi" -> highlight(params);
                        default -> "Unknown command\n";
                    };
                }
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
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException, Exception {
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
            System.out.printf("Welcome %s!%n", username);
            return help();
        }
        throw new ResponseException(400, "Expected: (R)egister <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException, Exception {
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

    public String logout() throws ResponseException, Exception {
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

    public HashSet<GameData> getGames() throws ResponseException, Exception {
        assertSignedIn();
        ListGames gameSet = server.listGames(authToken);
        if(gameSet == null || gameSet.games().isEmpty()){
            return null;
        }
        return gameSet.games();
    }


    public String listGames() throws ResponseException, Exception {
        HashSet<GameData> games = getGames();
        var result = new StringBuilder();
        for (var game : games) {
            result.append(game.gameID()).append(": ").append(game.gameName()).append('\n');
            result.append("White player: ").append(game.whiteUsername()).append("\n");
            result.append("Black player: ").append(game.blackUsername()).append("\n");
        }
        return result.toString();
    }

    public String createGame(String... params) throws ResponseException, Exception {
        assertSignedIn();
        if (params.length >= 1) {
            String gameName = params[0];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            server.createGame(gameName, authToken);
            return "Game Created";
        }
        throw new ResponseException(400, "Expected: (C)reate <Name_of_the_game>");
    }

    public String playGame(Object... params) throws ResponseException, Exception {
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
            HashSet<GameData> games = getGames();
            for(var game : games){
                if(game.gameID() == gameID){
                    ChessBoard.printBoard(game.game());
                    currentGame = game.game();
                    break;
                }
            }
            inGame = true;
            return "Joined Game";
        }
        throw new ResponseException(400, "Expected: (P)lay <(W)HITE or (B)LACK> <gameID>");
    }

    public String watchGame(Object... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            int gameID = Integer.parseInt((String) params[0]);
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);
            ChessBoard.main(new String[0]);
            inGame = true;
            return "Joined Game";
        }
        throw new ResponseException(400, "Expected: (P)lay <WHITE or BLACK> <gameID>");
    }

    public String makeMove(String... params) throws ResponseException, Exception {
        if (params.length >= 2) {
            String fromPos = params[0];
            String toPos = params[1];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);

            // Do the move
            // print the new board
            // Print the move to everyone else
            // If in check/mate/stalemate, notifiy the others
            return String.format("Move made: get rid of this %s", username);
        }
        throw new ResponseException(400, "Expected: (M)ake <beginning position> <ending position>");
    }

    public String highlight(String... params) throws ResponseException, Exception {
        if (params.length >= 1) {
            String piecePos = params[0];
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(username);

            // Validate the piece
            // Collect the moves
            // Print the highlighted board
            return "Highlighted";
        }
        throw new ResponseException(400, "Expected: (H)ighlight <position>");
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
        else if(inGame){
            return """
                Type...
                    'Redraw' or 'R': Redraws the board
                    'Highlight' or 'Hi': type a position where you'd
                        like to see the possible moves for a piece.
                    'Leave' or 'L': Leave the game (you can rejoin later,
                    or someone else can take your place.
                    'Resign' or 'Q': Resign the game. Better luck next time!
                    'Help' or 'H': Show possible actions
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