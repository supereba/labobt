package be.businesstraining.domain;

import java.util.Base64;

public class BasicTokenUtils {

    public static String BuildBasicToken(String user, String password) {
        String original = String.format("%s:%s", user, password);
        return Base64.getEncoder().encodeToString(original.getBytes());
    }

    public static String DecodeBasictoken(String token) {
        return new String(Base64.getDecoder().decode(token));
    }
}
