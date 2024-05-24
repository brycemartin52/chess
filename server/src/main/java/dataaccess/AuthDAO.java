package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AuthDAO implements AuthDAOInterface{
    static HashMap<String, AuthData> authData;

    public AuthDAO(){}

    public AuthData addAuth(String username){
        String authToken = createAuth(username);
        AuthData authDat = new AuthData(authToken, username);
        authData.put(authToken, authDat);
        return authDat;
    }

    @Override
    public AuthData getAuth(String authToken) {
        if(!authToken.isEmpty() && authData.containsKey(authToken)){
            return authData.get(authToken);
        }
        else{
            return null;
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        if(!authToken.isEmpty()){
            authData.remove(authToken);
        }
    }

    @Override
    public void clear() {
        authData = new HashMap<>();
    }
}
