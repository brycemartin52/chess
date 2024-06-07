package client;

import java.util.Arrays;

import chess.ChessGame;
import com.google.gson.Gson;
//import model.Pet;
//import model.PetType;
import exception.ResponseException;
//import client.websocket.NotificationHandler;
//import client.websocket.WebSocketFacade;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    boolean loggedIn;
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
            System.out.println("(P)lay Game <Game ID>");
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
                    case "All", "A" -> listGames();
                    case "Play", "P" -> "It's about to get crazy";
                    case "Watch", "W" -> "Watch an epic showdown";
                    case "Help", "H" -> help();
                    case "Logout", "O" -> logout();
                    default -> "Unknown command";
                };
            }
            else{
                return switch (cmd) {
                    case "Register", "R" -> "Sign up to play Chess";
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

    public String Register(String... params) throws ResponseException {
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

//    public String rescuePet(String... params) throws ResponseException {
//        assertSignedIn();
//        if (params.length >= 2) {
//            var name = params[0];
//            var type = PetType.valueOf(params[1].toUpperCase());
//            var pet = new Pet(0, name, type);
//            pet = server.addPet(pet);
//            return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
//        }
//        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG>");
//    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

//    public String adoptPet(String... params) throws ResponseException {
//        assertSignedIn();
//        if (params.length == 1) {
//            try {
//                var id = Integer.parseInt(params[0]);
//                var pet = getPet(id);
//                if (pet != null) {
//                    server.deletePet(id);
//                    return String.format("%s says %s", pet.name(), pet.sound());
//                }
//            } catch (NumberFormatException ignored) {
//            }
//        }
//        throw new ResponseException(400, "Expected: <pet id>");
//    }
//
//    public String adoptAllPets() throws ResponseException {
//        assertSignedIn();
//        var buffer = new StringBuilder();
//        for (var pet : server.listPets()) {
//            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//        }
//
//        server.deleteAllPets();
//        return buffer.toString();
//    }

    public String logout() throws ResponseException {
        assertSignedIn();
//        ws.leavePetShop(visitorName);
//        ws = null;
        loggedIn = false;
        return String.format("%s left the shop", username);
    }

//    private ChessGame getGame(int id) throws ResponseException {
//        for (var game : server.listGames()) {
//            if (game.gameid() == id) {
//                return game;
//            }
//        }
//        return null;
//    }

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