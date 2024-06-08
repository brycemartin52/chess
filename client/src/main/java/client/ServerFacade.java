package client;

import model.GameData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;
    }

    public Object makeRequest(String endpoint, String requestMethod) {
        try{
            URL url = new URL(serverUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");

            //if there's a body I'm trying to write (ie not list games).
//            if(){
                connection.setDoOutput(true);
                try(OutputStream requestBody = connection.getOutputStream();) {
                    // Write request body to OutputStream ...
                }
//            }



            // Set HTTP request headers, if necessary
            // connection.addRequestProperty("Accept", "text/html");
            connection.connect();



            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get HTTP response headers, if necessary
                // Map<String, List<String>> headers = connection.getHeaderFields();

                // OR

                //connection.getHeaderField("Content-Length");

                InputStream responseBody = connection.getInputStream();
                // Read response body from InputStream ...
            }
            else {
                // SERVER RETURNED AN HTTP ERROR

                InputStream responseBody = connection.getErrorStream();
                // Read and process error response body from InputStream ...
            }
        }
        catch(IOException e){
            System.out.println("Something went wrong with your make Request");
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void login(String username, String password) {
        makeRequest("/session", "POST");
    }

    public void register(String username, String password, String email) {
    }

    public void createGame() {
    }

    public Collection<GameData> listGames() {
        Collection<GameData> games = null;
        return games;
    }

    public void playGame() {
    }

    public void logout() {
    }
}
