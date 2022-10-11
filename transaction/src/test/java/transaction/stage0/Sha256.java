package transaction.stage0;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 {

    public static String encrypt(String text) {
        try {
            final var messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(final byte[] bytes) {
        final var builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
