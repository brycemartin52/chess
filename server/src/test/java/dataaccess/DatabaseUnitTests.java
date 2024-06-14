package dataaccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import utils.Encrypt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseUnitTests {

    private static UserData user;

    private static SQLAuthDAO adao;
    private static SQLGameDAO gdao;
    private static SQLUserDAO udao;

    @BeforeAll
    public static void mainInit() {
        try {
            adao = new SQLAuthDAO();
            udao = new SQLUserDAO();
            gdao = new SQLGameDAO();

            user = new UserData("user", "password", "email");
        }
        catch (Exception e){
            System.out.println("There was an error initializing the testing.");
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void del() {
        try {
            gdao.clear();
            adao.clear();
            udao.clear();
        }
        catch (Exception e){
            System.out.println("There was an error in clearing:");
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Clearing")
    public void clearTest() {
        try {
            gdao.clear();
            adao.clear();
            udao.clear();
        }
        catch (Exception e){
            System.out.println("There was an error:");
            System.out.println(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Good")
    public void registerTest() {
        try{
            int testID = gdao.createGame("New Game");
            Assertions.assertEquals(1, testID);
        }
        catch (DataAccessException e){
            System.out.println("Creating a game throws an error");
            Assertions.fail();
        }
    }

    @Test
    @Order(3)
    @DisplayName("Create Game Bad")
    public void registerBadTest() {
        try{
            int testID = gdao.createGame(null);
            System.out.println("Creating a game with a null name shouldn't work");
            Assertions.assertNotEquals(1, testID);
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Get Game Good")
    public void getGameGood() {
        try{
            int testID = gdao.createGame("New Game");
            GameData gdata = gdao.getGame(testID);
            assert(gdata != null);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(5)
    @DisplayName("Get Game bad")
    public void getGameBad() {
        try{
            int testID = gdao.createGame("New Game");
            GameData gdata = gdao.getGame(2);
            Assertions.assertNull(gdata);
        }
        catch (DataAccessException e){

            Assertions.fail();
        }
    }


    @Test
    @Order(6)
    @DisplayName("List Games good")
    public void listGamesGood() {
        try{
            int testID = gdao.createGame("New Game");
            int otherID = gdao.createGame("Other Games");
            HashSet<GameData> map = gdao.listGames();
            Assertions.assertFalse(map.isEmpty());
//            Assertions.assertTrue(map.contains(otherID));
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("List Games bad")
    public void listGamesBad() {
        try{
            HashSet<GameData> map = gdao.listGames();
            Assertions.assertTrue(map.isEmpty());
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(8)
    @DisplayName("Update Game Good")
    public void updateGame() {
        try{
            int testID = gdao.createGame("New Game");
            GameData gData = gdao.getGame(testID);
            GameData newGData = new GameData(testID, "username", "username2", "New Game", gData.game(), false);
            gdao.updateGame(newGData);
            GameData finalGData = gdao.getGame(testID);
            Assertions.assertNotEquals(gData, finalGData);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(8)
    @DisplayName("Update Game Bad")
    public void updateGameBad() {
        try{
            int testID = gdao.createGame("New Game");
            GameData gData = gdao.getGame(testID);
            GameData newGData = new GameData(1, "username", "username2", "Other Game", gData.game(), false);
            gdao.updateGame(newGData);
            GameData finalGData = gdao.getGame(testID);
            Assertions.assertNotEquals(gData, finalGData);
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(9)
    @DisplayName("addAuthTest")
    public void addAuthTest() {
        try{
            AuthData aData = adao.addAuth(user.username());
            Assertions.assertTrue(aData != null && aData.authToken() != null);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(9)
    @DisplayName("addAuthTestBad")
    public void addAuthTestBad() {
        try{
            AuthData aData = adao.addAuth(null);
            Assertions.fail();
        }
        catch (Exception e){
            System.out.println("Doesn't work");
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(10)
    @DisplayName("getAuthTest")
    public void getAuthTest() {
        try{
            AuthData aData = adao.addAuth(user.username());
            AuthData sameData = adao.getAuth(aData.authToken());
            Assertions.assertEquals(aData, sameData);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }


    @Test
    @Order(10)
    @DisplayName("getAuthTestBad")
    public void getAuthTestBad() {
        try{
            AuthData aData = adao.addAuth(user.username());
            AuthData sameData = adao.getAuth("bad authToken");
            Assertions.assertNull(sameData);
        }
        catch (DataAccessException e){
            System.out.println("Bad authToken not in database");
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(11)
    @DisplayName("deleteAuthTest")
    public void deleteAuthTest() {
        try{
            AuthData aData = adao.addAuth(user.username());
            Assertions.assertTrue(adao.deleteAuth(aData.authToken()));
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(11)
    @DisplayName("deleteAuthTest")
    public void deleteAuthTestBad() {
        try{
            AuthData aData = adao.addAuth(user.username());
            Assertions.assertFalse(adao.deleteAuth("bad authToken"));
            adao.deleteAuth(aData.authToken());
            Assertions.assertFalse(adao.deleteAuth(aData.authToken()));
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(12)
    @DisplayName("createUserTest")
    public void createUserTest() {
        try{
            udao.createUser(user);
            UserData uData = udao.getUser(user.username());
            Assertions.assertNotNull(uData);
            Assertions.assertEquals("user", uData.username());
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(12)
    @DisplayName("createUserTestBad")
    public void createUserTestBad() {
        try{
            udao.createUser(new UserData(null, user.password(), "email"));
            System.out.println("Doesn't work");
            Assertions.fail();
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @Order(13)
    @DisplayName("getUserTest")
    public void getUserTest() {
        try{
            udao.createUser(user);
            UserData uData = udao.getUser(user.username());
            Assertions.assertNotNull(uData);
            Assertions.assertNotEquals(uData.password(), "password");
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(13)
    @DisplayName("getUserTestBad")
    public void getUserTestBad() {
        try{
            udao.clear();
            UserData uData = udao.getUser(user.username());
            Assertions.assertNull(uData);
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }

    @Test
    @Order(14)
    @DisplayName("getPasswordTest")
    public void getPasswordTest() {
        try{
            udao.createUser(user);
            String pass = udao.getPassword(user.username());
            Assertions.assertTrue(pass != null && !Objects.equals(pass, "password"));
        }
        catch (DataAccessException e){
            System.out.println("Doesn't work");
            Assertions.fail();
        }
    }


    @Test
    @Order(14)
    @DisplayName("getPasswordTestBad")
    public void getPasswordTestBad() {
        try{
            udao.createUser(new UserData(null, user.password(), "email"));
            String pass = udao.getPassword(user.username());
            Assertions.assertTrue(pass != null && !Objects.equals(pass, "password"));
            System.out.println("Doesn't work");
            Assertions.fail();
        }
        catch (DataAccessException e){
            Assertions.assertTrue(true);
        }
    }
}
