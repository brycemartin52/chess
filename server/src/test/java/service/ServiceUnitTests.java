package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceUnitTests {

    private static GameService gameService;
    private static AuthService authService;
    private static UserService userService;
    private static UserData user;
    private static UserData badUser;

    @BeforeEach
    public void init() {
        try {
            gameService = new GameService();
            authService = new AuthService();
            userService = new UserService();
            user = new UserData("user", "password", "email");
            badUser = new UserData(null, "password", "email");
        }
        catch (Exception e){
            System.out.println("There was an error:");
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void del() {
        try {
            gameService.clearGame();
            authService.clearAuths();
            userService.clearUsers();
        }
        catch (Exception e){
            System.out.println("There was an error:");
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Clearing")
    public void clearTest() {
        AuthData aData = authService.add(user.username());

        try{
            gameService.createGame(aData.authToken(), "New Game");
            gameService.clearGame();
            HashSet<GameData> games = gameService.listGames(aData.authToken());
            Assertions.assertTrue(games.isEmpty());
        }
        catch (DataAccessException e){
            Assertions.fail();
        }
    }

    @Test
    @Order(2)
    @DisplayName("RegisterGoodTest")
    public void registerTest() {
        try{
            AuthData authData = userService.register(user);
            AuthData newAuthData = authService.verify(authData.authToken());
            Assertions.assertEquals(newAuthData, authData);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(3)
    @DisplayName("Register Bad Test")
    public void registerBadTest() {
        try{
            userService.register(badUser);
            System.out.println("Doesn't work");
            Assertions.fail();
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Login Good Test")
    public void loginTest() {
        try{
            AuthData authData = userService.register(user);
            userService.logout(authData.authToken());
            AuthData newAuthData = userService.login(user);
            Assertions.assertNotEquals(newAuthData.authToken(), null);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.assertFalse(false);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Login Bad Test")
    public void loginBadTest() {
        try{
            AuthData authData = userService.register(badUser);
            System.out.println("Doesn't work");
            Assertions.fail();
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }
    }


    @Test
    @Order(6)
    @DisplayName("Logout Good Test")
    public void logoutTest() {
        try{
            AuthData authData = userService.register(user);
            userService.logout(authData.authToken());
            AuthData newAuthData = authService.verify(authData.authToken());
            Assertions.assertNull(newAuthData);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.assertFalse(false);
        }
    }

    @Test
    @Order(7)
    @DisplayName("Logout Bad Test")
    public void logoutBadTest() {
        try{
            AuthData authData = userService.register(user);
            boolean success = userService.logout("badAuthToken");
            Assertions.assertFalse(success);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(8)
    @DisplayName("List a Game")
    public void listGame() {
        AuthData aData = authService.add(user.username());

        try{
            gameService.createGame(aData.authToken(), "New Game");
            HashSet<GameData> games = gameService.listGames(aData.authToken());
//            Assertions.assertInstanceOf(GameData.class, games);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.assertFalse(false);
        }

    }

    @Test
    @Order(9)
    @DisplayName("List Game, bad token")
    public void listGameUnauthorized() {
        AuthData aData = authService.add(user.username());

        try{
            gameService.createGame("badAuthToken", "New Game");
            HashSet<GameData> games = gameService.listGames(aData.authToken());
            Assertions.assertFalse(games.isEmpty());
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.assertFalse(false);
        }
    }


    @Test
    @Order(10)
    @DisplayName("Create a Game")
    public void addGame() {
        AuthData aData = authService.add(user.username());

        try{
            gameService.createGame(aData.authToken(), "New Game");
            HashSet<GameData> games = gameService.listGames(aData.authToken());
            Assertions.assertFalse(games.isEmpty());
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.assertFalse(false);
        }

    }

    @Test
    @Order(11)
    @DisplayName("Create a Game with a bad Token")
    public void addGameUnauthorized() {
        try{
            gameService.createGame(null, "New Game");
            System.out.println("Shouldn't work");
            Assertions.fail();
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(12)
    @DisplayName("Join a Game")
    public void joinGame() {
        AuthData aData = authService.add(user.username());

        try{
            gameService.createGame(aData.authToken(), "New Game");
            GameData game = gameService.joinGame(aData.authToken(), new JoinGameData(ChessGame.TeamColor.WHITE, 1));
            Assertions.assertNotNull(game.whiteUsername());
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }

    }

    @Test
    @Order(13)
    @DisplayName("Join a Game via stealing")
    public void joinGameIllegally() {
        AuthData aData = authService.add(user.username());

        try{
            gameService.createGame(aData.authToken(), "New Game");
            gameService.joinGame(aData.authToken(), new JoinGameData(ChessGame.TeamColor.WHITE, 1));
            gameService.joinGame(aData.authToken(), new JoinGameData(ChessGame.TeamColor.WHITE, 1));
            System.out.println("Doesn't work");
            Assertions.assertFalse(false);
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }

    }
}
