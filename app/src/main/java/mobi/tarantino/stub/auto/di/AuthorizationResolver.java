package mobi.tarantino.stub.auto.di;

import android.text.TextUtils;

import mobi.tarantino.stub.auto.IPreferencesManager;


public class AuthorizationResolver {

    private IPreferencesManager preferencesManager;

    public AuthorizationResolver(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    public boolean isAuthorized() {
        return !TextUtils.isEmpty(preferencesManager.getToken());
    }

    public String getToken() {
        return preferencesManager.getToken();
    }
}
