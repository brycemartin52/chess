package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;

public class AuthService {
    UserDAO udao;
    AuthDAO adao;

    public AuthService(){
        udao = new UserDAO();
        adao = new AuthDAO();
    }

    public AuthData verify(String authToken) {
        return adao.getAuth(authToken);
    }

    public boolean remove(String authToken) {
        return adao.deleteAuth(authToken);
    }

    public AuthData add(String username){
        return adao.addAuth(username);
    }

    public void clearAuths() { adao.clear();}
}
