package mobi.tarantino.stub.auto.di;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.BuildConfig;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalDataApi;
import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices;
import okhttp3.Interceptor;

@Module
public class AdditionalDataModule extends AbstractSSLModule {
    @Singleton
    @Provides
    public MobiAdditionalDataApi providesMobiAdditionalModel(X509TrustManager trustManager,
                                                             Logger logger) {
        return provideApi(trustManager, logger, MobiAdditionalDataApi.class);
    }

    @NonNull
    @Override
    protected String getApiUrl() {
        return BuildConfig.ADDITIONAL_DATA_API;
    }

    @Override
    protected Interceptor getAuthInterceptor() {
        return null;
    }

    public Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FuelPrices.class, new FuelPrices());
        return builder.create();
    }
}
