package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;

import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;

public class ServiceTests {

    GameService gameService;
    AuthService authService;
    UserService userService;
    UserData user;

    @BeforeAll
    public static void init() {
        GameService gameService = new GameService();
        AuthService authService = new AuthService();
        UserService userService = new UserService();
        UserData user = new UserData("user", "password", "email");
    }

    @Test
    @Order(1)
    @DisplayName("Add/Remove Legit AuthToken")
    public void addToken() {
        AuthData aData = authService.add(user.username());
        AuthData storedData = authService.verify(aData.authToken());
    }

//    @Test
//    @Order(3)
//    @DisplayName("Login Invalid User")
//    public void loginInvalidUser() {
//        TestAuthResult loginResult = serverFacade.login(newUser);
//
//        assertHttpUnauthorized(loginResult);
//        assertAuthFieldsMissing(loginResult);
//    }
}
