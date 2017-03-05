package mobi.tarantino.stub.auto.di;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.IOException;

import javax.inject.Singleton;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.BuildConfig;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.model.auth.MobiAuthApi;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static mobi.tarantino.stub.auto.BuildConfig.AUTH_API;

@Module
public class AuthApiModule extends AbstractSSLModule {
    @NonNull
    protected String getApiUrl() {
        return AUTH_API;
    }

    @Override
    @NonNull
    protected Interceptor getAuthInterceptor() {
        String credentials = BuildConfig.CLIENT_ID + ":" + BuildConfig.CLIENT_SECRET;
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    @Provides
    @Singleton
    public MobiAuthApi providesAuthApi(X509TrustManager trustManager, Logger logger) {
        return provideApi(trustManager, logger, MobiAuthApi.class);
    }

}
