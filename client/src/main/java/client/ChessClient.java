package client;

import java.util.*;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListGames;
import ui.ChessBoard;
import ui.EscapeSequences;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.RESET;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class ChessClient implements NotificationHandler{
    private String username;
    private String authToken;
    private final ServerFacade server;
    private final String serverUrl;
    private boolean loggedIn;
    private boolean inGame;
    private GameData currentGameData;
    private ChessGame.TeamColor team;
    private WebSocketFacade ws;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
                else if(team != null){
                    return switch (cmd) {
                        case "help", "h" -> help();
                        case "redraw", "r" -> ChessBoard.printBoard(currentGameData.game(), team, null);
                        case "leave", "l" -> leaveGame();
                        case "resign", "q" -> resign();
                        case "move", "m" -> makeMove(params);
                        case "highlight", "hi" -> highlight(params);
                        default -> "Unknown command\n";
                    };
                }
                else{
                    return switch (cmd) {
                        case "help", "h" -> help();
                        case "redraw", "r" -> ChessBoard.printBoard(currentGameData.game(), team,null);
                        case "leave", "l" -> leaveGame();
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
        server.logout(authToken);
        String message = String.format("See you next time, %s.", username);
        username = null;
        authToken = null;
        loggedIn = false;
        return message;
    }

    public GameData getGame(int gameID) throws Exception{
        HashSet<GameData> games = getGames();
        if(!(games == null)){
            for (var game : games) {
                if(game.gameID() == gameID){
                    return game;
                }
            }
        }
        return null;
    }

    public HashSet<GameData> getGames() throws ResponseException, Exception {
        assertSignedIn();
        ListGames gameSet = server.listGames(authToken);
        if(gameSet == null || gameSet.games() == null){
            return null;
        }
        return gameSet.games();
    }


    public String listGames() throws ResponseException, Exception {
        HashSet<GameData> games = getGames();
        if(games.isEmpty()){
            return "There are no games. Create one to get started!";
        }
        var result = new StringBuilder();
        for (var game : games) {
            result.append(game.gameID()).append(": ").append(game.gameName()).append('\n');
            result.append("White player: ").append(game.whiteUsername()).append("\n");
            result.append("Black player: ").append(game.blackUsername());
        }
        return result.toString();
    }

    public String createGame(String... params) throws ResponseException, Exception {
        assertSignedIn();
        if (params.length >= 1) {
            String gameName = params[0];
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
            if(getGame(gameID) == null){
                return "This is not a valid game number. Type (A)ll to see a list of the current games";
            }
            ChessGame.TeamColor team = switch (color){
                case "WHITE", "W", "w" -> ChessGame.TeamColor.WHITE;
                case "BLACK", "B", "b" -> ChessGame.TeamColor.BLACK;
                default -> throw new ResponseException(403, "Unexpected value: " + color);
            };
            this.team = team;
            server.playGame(team, gameID, authToken);
            inGame = true;
            UserGameCommand data = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, null);
            WebSocketFacade ws = new WebSocketFacade(this, serverUrl);
            ws.update(data);
            currentGameData = getGame(gameID);
            return "Joined Game";
        }
        throw new ResponseException(400, "Expected: (P)lay <(W)HITE or (B)LACK> <gameID>");
    }

    public String watchGame(Object... params) throws Exception {
        assertSignedIn();
        if (params.length >= 1) {
            int gameID = Integer.parseInt((String) params[0]);
            if(getGame(gameID) == null){
                return "This is not a valid game number. Type (A)ll to see a list of the current games";
            }
            UserGameCommand data = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, currentGameData.gameID(), null);
            WebSocketFacade ws = new WebSocketFacade(this, serverUrl);
            ws.update(data);
            ChessBoard.main(new String[0]);
            inGame = true;
            return "Joined Game";
        }
        throw new ResponseException(400, "Expected: (P)lay <WHITE or BLACK> <gameID>");
    }

    public static ChessPosition convertToPos(String input) {
        if (input == null || input.length() != 2) {
            throw new IllegalArgumentException("Input must be a two-character string.");
        }

        int column = input.charAt(0) - 'a' + 1;
        int row = Character.getNumericValue(input.charAt(1));

        if (column < 1 || column > 8 || row < 1 || row > 8) {
            throw new IllegalArgumentException("Input must be within the range 'a1' to 'h8'.");
        }

        return new ChessPosition(row, column);
    }

    public String makeMove(String... params) throws ResponseException, Exception {
        if (params.length >= 2) {
            throwIfGameOver();
            ChessPosition fromPos = convertToPos(params[0]);
            ChessPosition toPos = convertToPos(params[1]);
            ChessPiece.PieceType promotion;
            if (params.length == 2) {
                promotion = null;
            }
            else{
                promotion = switch (params[2].toLowerCase()){
                    case "queen", "q" -> ChessPiece.PieceType.QUEEN;
                    case "rook", "r" -> ChessPiece.PieceType.ROOK;
                    case "bishop", "b" -> ChessPiece.PieceType.BISHOP;
                    case "knight", "k" -> ChessPiece.PieceType.KNIGHT;
                    default -> throw new IllegalArgumentException(String.format("'%s' is an invalid promotion piece", params[2]));
                };
            }

            ChessMove attemptedMove = new ChessMove(fromPos, toPos, promotion);
            UserGameCommand data = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, currentGameData.gameID(), attemptedMove);
            WebSocketFacade ws = new WebSocketFacade(this, serverUrl);
            ws.update(data);
            return "";
        }
        throw new ResponseException(400, "Expected: (M)ake <beginning position> <ending position>");
    }

    private ArrayList<ChessPosition> getHighlightPos(ArrayList<ChessMove> moves){
        ArrayList<ChessPosition> positions = new ArrayList<>();
        positions.add(moves.getFirst().getStartPosition());
        for(var move : moves){
            positions.add(move.getEndPosition());
        }
        return positions;
    }

    public String highlight(String... params) throws ResponseException, Exception {
        if (params.length >= 1) {
            String piecePos = params[0];
            ChessPosition position;
            try{
                position = convertToPos(piecePos);
            }
            catch(IllegalArgumentException e){
                throw new IllegalArgumentException(e.getMessage());
            }
            if(currentGameData.game().getBoard().getPiece(position) == null){
                throw new IllegalArgumentException(String.format("There is no piece on position %s", position));
            }
            ArrayList<ChessMove> allMoves = (ArrayList<ChessMove>) currentGameData.game().validMoves(position);
            ChessBoard.printBoard(currentGameData.game(), team, getHighlightPos(allMoves));
            return "Highlighted";
        }
        throw new ResponseException(400, "Expected: (H)ighlight <position>");
    }

    public String leaveGame() throws Exception {
        team = null;
        inGame = false;
        UserGameCommand data = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, currentGameData.gameID(), null);
        WebSocketFacade ws = new WebSocketFacade(this, serverUrl);
        ws.update(data);
        return "Game left";
    }

    public String resign() throws Exception {
        UserGameCommand data = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, currentGameData.gameID(), null);
        WebSocketFacade ws = new WebSocketFacade(this, serverUrl);
        ws.update(data);

        return "Resigned. Better luck next time!";
    }

    public String help() {
        return ClientMenu.displayHelpMenu(loggedIn, inGame, team);
    }

    public void displayMenu(){
        ClientMenu.displayMenu(loggedIn, inGame);
    }

    private void assertSignedIn() throws ResponseException {
        if (!loggedIn) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    private void throwIfGameOver() throws Exception {
        if(currentGameData.finished()){
            throw new Exception("Cannot make a move when the game is finished");
        }
    }

    @Override
    public void notify(ServerMessage notification) {
        ServerMessage.ServerMessageType messageType = notification.getServerMessageType();
        if(messageType == ServerMessage.ServerMessageType.ERROR){
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + notification.getErrorMessage());
        }
        else if(messageType == ServerMessage.ServerMessageType.LOAD_GAME){
            try{
                currentGameData = getGame(currentGameData.gameID());
                ChessBoard.printBoard(currentGameData.game(), team,null);
                System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_GREEN);
            }
            catch(Exception e){
                System.out.println("Unable to fetch the game for update");
            }
        }
    }
}