package hello.tumblbug.domain.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncrypt { //해싱을 이용한 단방향 암호화

    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[18];
        random.nextBytes(bytes);
        byte[] encode = Base64.getEncoder().encode(bytes);
        return new String(encode); //length : 24
    }

    public static String encrypt(String raw, String salt) throws NoSuchAlgorithmException {
        String rawAndSalt = raw + salt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(rawAndSalt.getBytes());
        StringBuffer sb = new StringBuffer();
        for (byte b : md.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString(); //length : 64
    }
}
