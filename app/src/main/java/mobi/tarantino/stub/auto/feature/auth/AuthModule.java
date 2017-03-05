package mobi.tarantino.stub.auto.feature.auth;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.model.auth.AuthModel;

@Module
public class AuthModule {

    private AuthActivity authActivity;

    public AuthModule(AuthActivity authActivity) {
        this.authActivity = authActivity;
    }

    @Provides
    @AuthScope
    public InputCodeFragment.AuthSuccessListener provideAuthSuccessListener() {
        return authActivity;
    }

    @Provides
    @AuthScope
    public InputPhoneFragment.InputPhoneListener providePhoneSuccessListener() {
        return authActivity;
    }

    @NonNull
    @Provides
    @AuthScope
    public AuthPresenter provideAuthPresenter(AuthModel model, IPreferencesManager
            preferencesManager) {
        return new AuthPresenter(model, preferencesManager);
    }

    @NonNull
    @Provides
    @AuthScope
    public SmsSendTimerPresenter provideSmsSendTimerPresenter(AuthModel model) {
        return new SmsSendTimerPresenter(model);
    }
}
