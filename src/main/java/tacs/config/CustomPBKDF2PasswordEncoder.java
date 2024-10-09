package tacs.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CustomPBKDF2PasswordEncoder implements PasswordEncoder {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            String salt = generateSalt();
            String hash = hashPassword(rawPassword.toString(), salt);
            return salt + ":" + hash;
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing a password", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return rawPassword.toString().equals(encodedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying a password", e);
        }
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) throws Exception {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
}
