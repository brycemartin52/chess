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
import java.util.HashMap;

public class ServerFacade {
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData login(String username, String password) {
        UserData user = new UserData(username, password, null);
        return makeRequest("POST", "/session", user, AuthData.class, null);
    }

    public AuthData register(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        return makeRequest("POST", "/user", user, AuthData.class, null);
    }

    public GameData createGame(String gameName, String authToken) {
        GameData game = new GameData(0, null, null, gameName, null);
        return makeRequest("POST", "/game", game, GameData.class, authToken);
    }

    public HashMap listGames(String authToken) {
        return makeRequest("GET", "/game", null, HashMap.class, authToken);
    }

    public void playGame(String color, int gameID, String authToken) {
        ChessGame.TeamColor team = ChessGame.TeamColor.valueOf(color);
        JoinGameData data = new JoinGameData(team, gameID);
        makeRequest("PUT", "/game", data, null, authToken);
    }

    public void logout(String authToken) {
        makeRequest("DELETE", "/session", null, null, authToken);
    }

    public void clear() {
        makeRequest("DELETE", "/db", null, null, null);
    }

    public <T> T makeRequest(String requestMethod, String endpoint, Object request, Class<T> responseClass, String header) {
        try{
            URL url = (new URI(serverUrl + endpoint)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod(requestMethod);
            connection.addRequestProperty("authorization", header);

            if(request != null){
                connection.setDoOutput(true);
                writeBody(request, connection);
            }

            connection.connect();
            throwIfNotSuccessful(connection);
            return readBody(connection, responseClass);
        }
        catch(Exception e){
            System.out.printf("Error: %s%n", e.getMessage());
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
