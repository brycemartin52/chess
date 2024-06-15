package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


public class WebSocketFacade extends Endpoint{
    private final String serverUrl;
    private NotificationHandler handler;


    public WebSocketFacade(NotificationHandler handler, String url) {
        serverUrl = url;
        this.handler = handler;
    }

    public UserGameCommand update(UserGameCommand data, String authToken) throws Exception {
        return makeRequest("WEBSOCKET", "/ws", data, UserGameCommand.class, authToken);
    }

    public <T> T makeRequest(String requestMethod, String endpoint, Object request, Class<T> responseClass, String header) throws Exception{
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
            throw new Exception(e.getMessage());
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

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
