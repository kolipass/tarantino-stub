package mobi.tarantino.stub.auto.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.inject.Singleton;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.PreferencesManager;
import mobi.tarantino.stub.auto.helper.AmountFormatter;
import mobi.tarantino.stub.auto.helper.ResourcesHelper;
import mobi.tarantino.stub.auto.helper.ValidatorHelper;
import mobi.tarantino.stub.auto.model.auth.HttpApiException;
import mobi.tarantino.stub.auto.model.location.LocationModel;
import mobi.tarantino.stub.auto.retrofitUtils.HttpLoggingInterceptor;
import mobi.tarantino.stub.auto.retrofitUtils.SSLPinningHelper;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.HttpApiExceptionStrategy;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.HttpExceptionStrategy;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.UnknownHostExceptionStrategy;
import retrofit2.adapter.rxjava.HttpException;

@Module
public class BaseModule {
    private Context context;

    public BaseModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

    @NonNull
    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public Picasso providesPicasso() {
        return Picasso.with(context);
    }

    @Provides
    @Singleton
    public Logger providesLogger() {
        return new Logger();
    }

    @Provides
    @Singleton
    public X509TrustManager provideX509TrustManager() {
        try {
            return SSLPinningHelper.initSSLPinning(context);
        } catch (@NonNull CertificateException | IOException | KeyStoreException |
                NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor.Logger providesHttpLoggingInterceptor() {
        return providesLogger();
    }

    @NonNull
    @Provides
    @Singleton
    public IPreferencesManager providesPreferencesManager(Logger logger) {
        return new PreferencesManager(context, logger);
    }

    @NonNull
    @Provides
    @Singleton
    public ValidatorHelper provideValidatorHelper() {
        return new ValidatorHelper();
    }

    @NonNull
    @Provides
    @Singleton
    public ResourcesHelper provideResourcesHelper() {
        return new ResourcesHelper(context);
    }

    @NonNull
    @Provides
    @Singleton
    public AmountFormatter provideDataFormatter() {
        return new AmountFormatter(context.getResources());
    }

    @NonNull
    @Provides
    @Singleton
    public LocationModel provideLocationModel(Context context) {
        return new LocationModel(context);
    }

    @NonNull
    @Provides
    @Singleton
    public AuthorizationResolver provideAuthorizationResolver(IPreferencesManager
                                                                      preferencesManager) {
        return new AuthorizationResolver(preferencesManager);
    }

    @Provides
    public ErrorHandler provideErrorHandler(IntentStarter intentStarter) {
        return new ErrorHandler(context, intentStarter)
                .registerStrategy(UnknownHostException.class, new UnknownHostExceptionStrategy())
                .registerStrategy(HttpApiException.class, new HttpApiExceptionStrategy())
                .registerStrategy(HttpException.class, new HttpExceptionStrategy());
    }
}