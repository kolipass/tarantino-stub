package mobi.tarantino.stub.auto.feature.splashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

import java.util.concurrent.TimeUnit;

import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.gcm.RegistrationIntentService;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TagManager tagManager = TagManager.getInstance(this.getApplicationContext());
        PendingResult<ContainerHolder> pending = tagManager.loadContainerDefaultOnly(
                "GTM-YYYYY",    // container ID of the form "GTM-XXXX"
                R.raw.gtm_5rlzdb   // the resource ID of the default container
        );
        pending.setResultCallback(new ResolvingResultCallbacks<ContainerHolder>(this, -1) {

            @Override
            public void onSuccess(@NonNull ContainerHolder containerHolder) {

                checkGooglePlay();
                finish();
            }

            @Override
            public void onUnresolvableFailure(@NonNull Status status) {
                Toast.makeText(SplashActivity.this, "status: " + status, Toast.LENGTH_LONG)
                        .show();
            }
        }, 2, TimeUnit.SECONDS);


    }

    private void checkGooglePlay() {
        MobiApplication.get(SplashActivity.this)
                .getComponentContainer()
                .start();
        if (isGoogleServicesAvailable()) {
            Intent registrationIntent = new Intent(SplashActivity.this, RegistrationIntentService
                    .class);
            registrationIntent.setAction(Consts.GCMRegistration.ACTION_REGISTRATION);
            startService(registrationIntent);
        } else {
            Toast.makeText(SplashActivity.this, getString(R.string
                    .update_play_services_notification), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean isGoogleServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        return status == ConnectionResult.SUCCESS;
    }
}
