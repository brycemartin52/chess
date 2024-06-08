package client;

import com.sun.tools.javac.Main;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.ByteArrayOutputStream;
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
    }

    @Test
    @Order(4)
    void logoutBad() throws Exception {
        facade.logout(aData.authToken());
        System.out.println("There should be an error message right here ^^^");
    }

    @Test
    @Order(5)
    void loginGood() throws Exception {
        AuthData auth = facade.login("player1", "password");
        Assertions.assertNotNull(auth);
    }

    @Test
    @Order(6)
    void loginBad() throws Exception {
        AuthData auth = facade.login("player", "password");
        System.out.println("There should be an error message right here ^^^");
        Assertions.assertNull(auth);
    }

}
