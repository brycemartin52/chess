package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.HashMap;

public class ServerFacade {
    private final String serverUrl;
    private final Gson serializer;

    public ServerFacade(String url) {
        serverUrl = url;
        serializer = new Gson();
    }

    public void login(String username, String password) {
        UserData user = new UserData(username, password, null);
        String userData = serializer.toJson(user);
        makeRequest("POST", "/session", userData, AuthData.class);
    }

    public void register(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        String userData = serializer.toJson(user);
        makeRequest("POST", "/user", userData, AuthData.class);
    }

    public void createGame(String gameName) {
        makeRequest("POST", "/game", gameName, Integer.class);
    }

    public HashMap<Integer, GameData> listGames() {
        return makeRequest("GET", "/game", null, HashMap.class);
    }

    public void playGame(String color, int gameID) {
        ChessGame.TeamColor team = ChessGame.TeamColor.valueOf(color);
        JoinGameData data = new JoinGameData(team, gameID);
        makeRequest("PUT", "/game", data, null);
    }

    public void logout() {
        makeRequest("DELETE", "/session", null, null);
    }

    public <T> T makeRequest(String requestMethod, String endpoint, Object request, Class<T> responseClass) {
        try{
            URL url = (new URI(serverUrl + endpoint)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod(requestMethod);

            if(request != null){
                writeBody(request, connection);
            }
            connection.connect();
            throwIfNotSuccessful(connection);
            return readBody(connection, responseClass);
        }
        catch(Exception e){
            System.out.printf("Something went wrong with your make request: %s%n", e.getMessage());
            return null;
        }
    }

    private static void writeBody(Object request, HttpURLConnection connection) throws IOException {
        if (request != null) {
            connection.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = connection.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
