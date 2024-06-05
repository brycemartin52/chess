package service;

import dataaccess.SQLAuthDAO;
import dataaccess.SQLUserDAO;
import model.AuthData;

public class AuthService {
    SQLUserDAO udao;
    SQLAuthDAO adao;

    public AuthService(){
        try {
            udao = new SQLUserDAO();
            adao = new SQLAuthDAO();
        }
        catch (Exception e){
            System.out.println("AuthService failed to start up.");
        }
    }

    public AuthData verify(String authToken) {
        try{
            return adao.getAuth(authToken);
        }
        catch(Exception e){
            System.out.println("The verify method failed with this message: ");
        }
        return null;
    }

    public boolean remove(String authToken) {
        try{
            return adao.deleteAuth(authToken);
        }
        catch(Exception e){
            System.out.println("The remove Auth method failed with this message: ");
        }
        return false;
    }

    public AuthData add(String username){
        try{
            return adao.addAuth(username);
        }
        catch(Exception e){
            System.out.println("The add AuthData method failed with this message: %n");
        }
        return null;
    }

    public void clearAuths() {
        try{
            adao.clear();
        }
        catch(Exception e){
            System.out.println("The ___ method failed with this message: ");
        }

    }
}
