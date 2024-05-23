package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class AuthDAO implements AuthDAOInterface{

    ArrayList<AuthData> allAuthData;
    ArrayList<String> allAuthTokens;

    public AuthDAO(){
    }

    public void addAuth(String username) throws DataAccessException {
        String authToken = createAuth(username);
        allAuthTokens.add(authToken);
        allAuthData.add(new AuthData(authToken, username));
    }

    @Override
    public AuthData getAuth(String authToken) {
        if(!authToken.isEmpty() && allAuthTokens.contains(authToken)){
            for(AuthData dat : allAuthData){
                if(Objects.equals(dat.authToken(), authToken)){
                    return dat;
                }
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        if(!authToken.isEmpty() && allAuthTokens.contains(authToken)){
            allAuthTokens.remove(authToken);
            allAuthData.removeIf(dat -> Objects.equals(dat.authToken(), authToken));
        }
    }

    @Override
    public void clear() {
        allAuthData = new ArrayList<>();
        allAuthTokens = new ArrayList<>();
    }
}
