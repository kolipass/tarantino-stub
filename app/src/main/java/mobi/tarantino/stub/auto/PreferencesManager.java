package mobi.tarantino.stub.auto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import static mobi.tarantino.stub.auto.Consts.PreferencesKey.CARS_COUNT;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.DEMO_TOUR_FINISHED;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.DRIVERS_COUNT;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.FINES_NOT_PAY_COUNT;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.FINES_PAY_COUNT;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.GA_CLIENT_ID;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.GCM_IS_REGISTERED_ON_SERVER;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.GCM_TOKEN;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.NEW_FINES_COUNT;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.PHONE;
import static mobi.tarantino.stub.auto.Consts.PreferencesKey.TOKEN;

public class PreferencesManager implements IPreferencesManager {

    public static final String preferenceFileKey = "preference";
    private final Logger logger;
    private final Context context;

    private SharedPreferences preferences;

    private KeyStore keyStore;

    public PreferencesManager(Context context, Logger logger) {
        this.logger = logger;
        this.context = context;
        preferences = this.context.getSharedPreferences(preferenceFileKey,
                Context.MODE_PRIVATE);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                createNewKeys(context, TOKEN);
            }
        } catch (KeyStoreException | CertificateException | IOException |
                NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                NoSuchProviderException e) {
            logger.e(e);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void createNewKeys(Context context, String alias) throws
            KeyStoreException,
            CertificateException,
            NoSuchAlgorithmException,
            IOException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException {
        keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (!keyStore.containsAlias(alias)) {
            KeyPair keyPair = getKeyPair(context, alias);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private KeyPair getKeyPair(Context context, String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_DECRYPT)
                    .setDigests(KeyProperties.DIGEST_SHA256,
                            KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                    .build());

            return keyPairGenerator.generateKeyPair();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 1);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=Mobi Auto, O=Mobi"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            generator.initialize(spec);

            return generator.generateKeyPair();
        } else {
            return null;
        }

    }

    public Cipher getCipher() throws NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // below android m
                return Cipher.getInstance("RSA/ECB/PKCS1Padding"); // error in android 6:
                // InvalidKeyException: Need RSA private or public key
            } else { // android m and above
                return Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"); // error in
                // android 5: NoSuchProviderException: Provider not available:
                // AndroidKeyStoreBCWorkaround
            }
        } catch (Exception exception) {
            throw new RuntimeException("getCipher: Failed to get an instance of Cipher", exception);
        }
    }


    @Override
    @Nullable
    public String getToken() {
        String encryptedToken = preferences.getString(TOKEN, null);

        if (!TextUtils.isEmpty(encryptedToken) &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            try {
                return decryptString(TOKEN, encryptedToken, keyStore);
            } catch (Exception e) {
                logger.e(e);
                encryptedToken = null;
            }

        }
        return encryptedToken;
    }

    @Override
    public void setToken(@NonNull String token) {
        SharedPreferences.Editor editor = preferences.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                String encryptedToken = encryptString(TOKEN, token, keyStore);

                editor.putString(TOKEN, encryptedToken);
            } catch (Exception e) {
                logger.e(e);
            }
        } else {
            editor.putString(TOKEN, token);
        }
        editor.commit();
    }

    private String decryptString(String alias, String cipherText, KeyStore keyStore) throws
            IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, UnrecoverableEntryException, KeyStoreException {

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry
                (alias, null);
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        Cipher outputCipher = getCipher();
        outputCipher.init(Cipher.DECRYPT_MODE, privateKey);

        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), outputCipher);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i);
        }

        return new String(bytes, 0, bytes.length, "UTF-8");
    }

    private String encryptString(String alias, String initialText, KeyStore keyStore) throws
            UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry
                (alias, null);
        Key publicKey = privateKeyEntry.getCertificate().getPublicKey();

        Cipher input = getCipher();
        input.init(Cipher.ENCRYPT_MODE, publicKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(
                outputStream, input);
        cipherOutputStream.write(initialText.getBytes("UTF-8"));
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        return Base64.encodeToString(vals, Base64.DEFAULT);
    }

    @Override
    @Nullable
    public String getPhone() {
        return preferences.getString(PHONE, "");
    }

    @Override
    public void setPhone(@NonNull String phone) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PHONE, phone);
        editor.commit();

    }

    @Override
    public boolean isDemoTourFinished() {
        return preferences.getBoolean(DEMO_TOUR_FINISHED, false);
    }

    @Override
    public void demoTourFinish(boolean isFinish) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DEMO_TOUR_FINISHED, isFinish);
        editor.commit();
    }

    public void clearAll() {
        preferences.edit().clear().commit();
    }

    @Override
    public void setFinePayCount(String count) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FINES_PAY_COUNT, count);
        editor.commit();
    }

    @Override
    public void setFineNotPayCount(String count) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FINES_NOT_PAY_COUNT, count);
        editor.commit();
    }

    @Override
    public String getGaClientId() {
        return preferences.getString(GA_CLIENT_ID, "");
    }

    @Override
    public void setGaClientId(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(GA_CLIENT_ID, id);
        editor.commit();
    }

    @Override
    public String getFinesPayCount() {
        return preferences.getString(FINES_PAY_COUNT, "0");
    }

    @Override
    public String getFinesNotPayCount() {
        return preferences.getString(FINES_NOT_PAY_COUNT, "0");
    }

    @Override
    public String getDriversCount() {
        return preferences.getString(DRIVERS_COUNT, "0");
    }

    @Override
    public void setDriversCount(String count) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DRIVERS_COUNT, count);
        editor.commit();
    }

    @Override
    public String getCarsCount() {
        return preferences.getString(CARS_COUNT, "0");
    }

    @Override
    public void setCarsCount(String count) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CARS_COUNT, count);
        editor.commit();
    }

    @Override
    public int getNewFinesCount() {
        return preferences.getInt(NEW_FINES_COUNT, 0);
    }

    @Override
    public void setNewFinesCount(int count) {
        preferences.edit().putInt(NEW_FINES_COUNT, count).commit();
    }

    @Override
    public String getGcmToken() {
        return preferences.getString(GCM_TOKEN, "");
    }

    @Override
    public void setGcmToken(String token) {
        preferences.edit().putString(GCM_TOKEN, token).commit();
    }

    @Override
    public boolean isRegisterGcmOnServer() {

        return preferences.getBoolean(GCM_IS_REGISTERED_ON_SERVER, false);
    }

    @Override
    public void setRegisterGcmOnServer(boolean isRegistered) {
        preferences.edit().putBoolean(GCM_IS_REGISTERED_ON_SERVER, isRegistered);
    }

    @Override
    public void clearFines() {
        setNewFinesCount(0);
        setFineNotPayCount("0");
        setFinePayCount("0");
    }
}
