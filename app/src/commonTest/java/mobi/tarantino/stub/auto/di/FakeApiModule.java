package mobi.tarantino.stub.auto.di;

import mobi.tarantino.stub.auto.model.auth.MobiAuthApi;
import mobi.tarantino.stub.auto.retrofitUtils.HttpLoggingInterceptor;

import static org.mockito.Mockito.mock;

public class FakeApiModule extends AuthApiModule {

    public MobiAuthApi providesAuthApi(HttpLoggingInterceptor.Logger logger) {
        return mock(MobiAuthApi.class);
    }

}
