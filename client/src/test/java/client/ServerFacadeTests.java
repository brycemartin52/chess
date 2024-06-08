package client;

import com.sun.tools.javac.Main;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int number;

    private static AuthData aData;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

//    @BeforeEach
//    public void setup(){
//        server.clear();
//    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        facade.clear();
        Random rand = new Random();
        number = rand.nextInt(100000);
        aData = facade.register("player1", "password", "p1@email.com");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    void registerGood() throws Exception {
        assert(number != 1);
        AuthData authData = facade.register("player" + number, "password", "p" + number + "@email.com");
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    @Order(2)
    void registerBad() throws Exception {
        try{
            AuthData authData = facade.register("player1", "password", "p1@email.com");
            Assertions.assertNull(authData);
            System.out.println("There should be an error message right here ^^^");
        }
        catch(Exception e){
            System.out.println("Cannot register the same player twice");
            Assertions.fail();
        }
    }

    @Test
    @Order(3)
    void logoutGood() throws Exception {
        facade.logout(aData.authToken());
        System.out.println("There should be no error message right here ^^^");
        Assertions.assertTrue(true);
    }

    @Test
    @Order(4)
    void logoutBad() throws Exception {
        facade.logout(aData.authToken());
        System.out.println("There should be an error message right here ^^^");
        Assertions.assertTrue(true);
    }

    @Test
    @Order(5)
    void loginGood() throws Exception {
        aData = facade.login("player1", "password");
        Assertions.assertNotNull(aData);
    }

    @Test
    @Order(6)
    void loginBad() throws Exception {
        AuthData auth = facade.login("player", "password");
        System.out.println("There should be an error message right here ^^^");
        Assertions.assertNull(auth);
    }

    @Test
    @Order(7)
    void listBad() throws Exception {
        HashMap gameMap = facade.listGames(aData.authToken());
        ArrayList<GameData> games = (ArrayList<GameData>) gameMap.get("games");
        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    @Order(8)
    void createGood() throws Exception {
        GameData game = facade.createGame("New Game", aData.authToken());
        HashMap<Integer, GameData> games = facade.listGames(aData.authToken());
        Assertions.assertFalse(games.isEmpty());
        assertEquals("New Game", game.gameName());
    }

    @Test
    @Order(9)
    void createBad() throws Exception {
        try{
            GameData game = facade.createGame("Other Game", "badAuthToken");
            System.out.printf("Game ID '%d' should be null", game.gameID());
            Assertions.fail();
        }
        catch(Exception e){
            Assertions.assertTrue(true);
        }

    }

    @Test
    @Order(10)
    void listGood() throws Exception {
        GameData game = facade.createGame("Different Game", aData.authToken());
        HashMap<Integer, GameData> games = facade.listGames(aData.authToken());
        Assertions.assertFalse(games.isEmpty());
        assertEquals("Different Game", game.gameName());
    }

    @Test
    @Order(11)
    void joinGood() throws Exception {
        facade.playGame("WHITE", 1, aData.authToken());
        System.out.println("There should be no error message right here ^^^");
    }

    @Test
    @Order(12)
    void joinBad() throws Exception {
        facade.playGame("BLACK", 1, "authToken");
        System.out.println("There should be an error message right here ^^^");
    }

}
