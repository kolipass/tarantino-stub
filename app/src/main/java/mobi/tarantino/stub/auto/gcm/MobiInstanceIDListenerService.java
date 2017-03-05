package mobi.tarantino.stub.auto.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

import mobi.tarantino.stub.auto.Consts;

/**

 */

public class MobiInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent registrationIntent = new Intent(this, RegistrationIntentService.class);
        registrationIntent.setAction(Consts.GCMRegistration.ACTION_REGISTRATION);
        startService(registrationIntent);
    }
}
