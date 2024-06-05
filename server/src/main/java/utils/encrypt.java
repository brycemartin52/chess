package utils;

import org.mindrot.jbcrypt.BCrypt;

public class encrypt {
    public static String getHash(String string){
        return BCrypt.hashpw(string, BCrypt.gensalt());
    }

    public static boolean compareHash(String string, String hash){
        return BCrypt.checkpw(string, hash);
    }
}
