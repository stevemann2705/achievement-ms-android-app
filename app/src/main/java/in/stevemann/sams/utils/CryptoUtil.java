package in.stevemann.sams.utils;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import static android.content.ContentValues.TAG;

public class CryptoUtil {
    private static final String ALIAS = "ALIASTEXT";

    private static CryptoUtil singleton;

    public static CryptoUtil getInstance() {

        if (null == singleton) {
            synchronized (CryptoUtil.class){
                if (null == singleton) {
                    singleton = new CryptoUtil();
                }
            }
        }

        return singleton;
    }

    private CryptoUtil(){
    }

    public String decryptToken(String token, String iv) {
        Decrypter decrypter = null;

        try {
            decrypter = Decrypter.getInstance();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            e.printStackTrace();
        }

        String decryptedToken = null;

        try {
            decryptedToken = decrypter
                    .decryptData(ALIAS, Base64.decode(token, Base64.NO_WRAP), Base64.decode(iv, Base64.NO_WRAP));
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException |
                IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return decryptedToken;
    }

    public void encryptToken(String plainText, Context context) {

        Encrypter encrypter = null;

        try {
            encrypter = Encrypter.getInstance(ALIAS);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        try {
            encrypter.encryptText(plainText);
        } catch (IOException e) {
            Log.e(TAG, "onClick() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

    }

    public static void saveToken(Context context){
        Encrypter encrypter = null;

        try {
            encrypter = Encrypter.getInstance(ALIAS);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        TokenUtil.writeData(Objects.requireNonNull(encrypter).getIVString() + " " + encrypter.getEncryptionText(), context);
    }
}

class Decrypter {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private KeyStore keyStore;

    private static Decrypter singleton;

    public static Decrypter getInstance() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        if (null == singleton) {
            synchronized (Decrypter.class){
                if (null == singleton) {
                    singleton = new Decrypter();
                }
            }
        }

        return singleton;
    }

    private Decrypter() throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException {
        initKeyStore();
    }

    private void initKeyStore() throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
    }

    String decryptData(final String alias, final byte[] encryptedData, final byte[] encryptionIv)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchPaddingException, InvalidKeyException, IOException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIv, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec);

        return new String(cipher.doFinal(encryptedData), "UTF-8");
    }

    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            UnrecoverableEntryException, KeyStoreException {
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
    }
}

class Encrypter {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private String encryptionText;
    private byte[] encryption;
    private byte[] iv;
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);

    private static Encrypter singleton;

    public static Encrypter getInstance(final String alias) throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {

        if (null == singleton) {
            synchronized (Encrypter.class){
                if (null == singleton) {
                    singleton = new Encrypter(alias);
                }
            }
        }

        return singleton;
    }

    private Encrypter(final String alias) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));
        iv = cipher.getIV();
    }

    String encryptText(final String textToEncrypt)
            throws BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8"));
        encryptionText = Base64.encodeToString(encryption, Base64.NO_WRAP);
        return encryptionText;
    }

    @NonNull
    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());

        return keyGenerator.generateKey();
    }

    String getEncryptionText() {
        return encryptionText;
    }

    String getIVString() {
        return Base64.encodeToString(iv, Base64.NO_WRAP);
    }
}