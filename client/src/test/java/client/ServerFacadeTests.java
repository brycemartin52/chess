package client;

import com.sun.tools.javac.Main;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Random;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int number;

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
        Random rand = new Random();
        number = rand.nextInt(100000);
    }

    @AfterAll
    static void stopServer() {

        server.stop();
    }

    @Test
    void registerGood() throws Exception {
        AuthData authData = facade.register("player" + number, "password", "p" + number + "@email.com");
        Assertions.assertNotNull(authData.authToken());
        System.out.println(authData.authToken());
    }

    @Test
    void registerBad() throws Exception {
        try{
            AuthData authData = facade.register("player1", "password", "p1@email.com");
            AuthData authData2 = facade.register("player1", "password", "p1@email.com");
            Assertions.fail();
        }
        catch(Exception e){
            System.out.println("Cannot register the same player twice");
        }
    }

    @Test
    void logoutGood() throws Exception {
        try {
            AuthData authData = facade.register("player1", "password", "p1@email.com");
        } catch (Exception e) {
            System.out.println("Already registered");
        }
        facade.logout();
    }

}
