package mobi.tarantino.stub.auto.di;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.retrofitUtils.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AbstractSSLModule {
    protected X509TrustManager trustManager;

    protected <N> N provideApi(X509TrustManager trustManager, @NonNull Logger logger, @NonNull
            Class<N> service) {
        this.trustManager = trustManager;
        try {
            Retrofit retrofit = getRetrofit(logger);
            return retrofit.create(service);
        } catch (@NonNull NoSuchAlgorithmException
                | KeyStoreException
                | KeyManagementException e) {
            logger.e(e);
            throw new RuntimeException();
        }
    }

    @NonNull
    protected Retrofit getRetrofit(HttpLoggingInterceptor.Logger logger) throws
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return getBuilder(logger)
                .build();
    }

    @NonNull
    protected Retrofit.Builder getBuilder(HttpLoggingInterceptor.Logger logger) throws
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new Retrofit.Builder()
                .baseUrl(getApiUrl())
                .client(getOkHttpClient(logger))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()));
    }

    @NonNull
    protected abstract String getApiUrl();

    @NonNull
    protected OkHttpClient getOkHttpClient(HttpLoggingInterceptor.Logger logger) throws
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (getAuthInterceptor() != null) {
            builder.addInterceptor(getAuthInterceptor());
        }

        return builder
                .connectTimeout(3, TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor(logger))
                .sslSocketFactory(getSSLSocketFactory(), trustManager)
                .build();
    }

    protected abstract Interceptor getAuthInterceptor();

    @NonNull
    protected Interceptor getHttpLoggingInterceptor(HttpLoggingInterceptor.Logger logger) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @NonNull
    private SSLSocketFactory getSSLSocketFactory() throws NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, null);

        return sslContext.getSocketFactory();
    }

    public Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
}
