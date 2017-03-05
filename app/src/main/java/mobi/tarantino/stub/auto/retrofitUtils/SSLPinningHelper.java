package mobi.tarantino.stub.auto.retrofitUtils;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import mobi.tarantino.stub.auto.R;

/**
 * https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes
 * /CustomTrust.java
 */

public class SSLPinningHelper {
    @NonNull
    public static X509TrustManager initSSLPinning(@NonNull Context context) throws
            CertificateException,
            IOException, KeyStoreException, NoSuchAlgorithmException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // Create a KeyStore containing trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
//        keyStore.setCertificateEntry("intermediate_ca", getCertificate(context, cf, R.raw
// .intermediate_ca));
        keyStore.setCertificateEntry("github", getCertificate(context, cf, R.raw.github));

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
        trustManagerFactory.init(keyStore);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    @NonNull
    private static Certificate getCertificate(@NonNull Context context, @NonNull
            CertificateFactory cf, int id)
            throws CertificateException {
        InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(id));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ca;
    }
}
