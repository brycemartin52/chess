package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDAO implements AuthDAOInterface{
    static HashMap<String, AuthData> authData;

    public AuthDAO(){
        authData = new HashMap<>();
    }

    public AuthData addAuth(String username){
        String authToken = createAuth(username);
        AuthData authDat = new AuthData(authToken, username);
        authData.put(authToken, authDat);
        return authDat;
    }

    @Override
    public AuthData getAuth(String authToken) {
        if(authToken == null){
            return null;
        }
        if(!authToken.isEmpty() && authData.containsKey(authToken)){
            return authData.get(authToken);
        }
        else{
            return null;
        }
    }

    @Override
    public boolean deleteAuth(String authToken) {
        if(authData.containsKey(authToken)){
            authData.remove(authToken);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        authData = new HashMap<>();
    }
}
