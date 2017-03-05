package mobi.tarantino.stub.auto.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;

/**

 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "ReIntentService";

    @Inject
    IPreferencesManager preferencesManager;

    public RegistrationIntentService() {
        super(TAG);
    }

    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferencesManager = MobiApplication.get(this)
                .getComponentContainer()
                .getBaseComponent()
                .providesPreferencesManager();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Consts.GCMRegistration.ACTION_REGISTRATION:
                gcmRegistration();
                break;
            case Consts.GCMRegistration.ACTION_SAVE_TO_SERVER:
                registerTokenOnServer();
                break;
            case Consts.GCMRegistration.ACTION_LOGOUT:
                logout();
                break;
        }
    }

    private void logout() {

    }

    private void gcmRegistration() {
        String token = preferencesManager.getGcmToken();
        InstanceID instanceID = InstanceID.getInstance(this);
        if (TextUtils.isEmpty(token)) {
            try {
                token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                preferencesManager.setGcmToken(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerTokenOnServer() {

    }
}
