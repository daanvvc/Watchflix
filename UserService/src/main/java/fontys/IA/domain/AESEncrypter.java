package fontys.IA.domain;

import fontys.IA.configuration.EncryptionConfig;
import org.bouncycastle.crypto.OutputLengthException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AESEncrypter {
    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static SecretKey secretKey;

    public AESEncrypter(EncryptionConfig config) {
        secretKey = config.getKey();
    }

    public static String encrypt(String input) {
        try{
            byte[] initializationVector = new byte[IV_LENGTH];
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, initializationVector);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            byte[] cipherText = cipher.doFinal(input.getBytes());

            return Base64.getEncoder()
                    .encodeToString(cipherText);
        } catch (Exception ex) {
            throw new OutputLengthException(ex.getMessage());
            // TODO Exception handling
        }
    }

    public static String decrypt(String cipherText) {
        try{
            byte[] initializationVector = new byte[IV_LENGTH];
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, initializationVector);

            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
            return new String(plainText);
        } catch (Exception ex) {
            throw new OutputLengthException("IMPROVE");
            // TODO Exception handling
        }
    }
}
