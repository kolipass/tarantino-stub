package mobi.tarantino.stub.auto.feature.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.di.DependencyComponentManager;
import mobi.tarantino.stub.auto.gcm.RegistrationIntentService;

/**
 * A login screen that offers login via email/password.
 */
public class AuthActivity extends AppCompatActivity
        implements InputPhoneFragment.InputPhoneListener, InputCodeFragment.AuthSuccessListener {

    static final String PHONE_FRAGMENT_TAG = "phoneFragment";
    static final String AUTH_FRAGMENT_TAG = "authFragment";

    @Inject
    IntentStarter intentStarter;

    private DependencyComponentManager componentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        componentManager = MobiApplication.get(this).getComponentContainer();
        componentManager.getAuthComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        showInputPhoneFragment();
    }

    private void showInputPhoneFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, InputPhoneFragment.newInstance(),
                        PHONE_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phone_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAuthFragment(String phone) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, InputCodeFragment.newInstance(phone),
                        AUTH_FRAGMENT_TAG)
                .addToBackStack(PHONE_FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void onAuthSuccess() {
        this.finish();
        intentStarter.start();
        componentManager.releaseAuthComponent();
        registerGcmTokenOnServer();
    }

    private void registerGcmTokenOnServer() {
        Intent saveTokenIntent = new Intent(this, RegistrationIntentService.class);
        saveTokenIntent.setAction(Consts.GCMRegistration.ACTION_SAVE_TO_SERVER);
        startService(saveTokenIntent);
    }

    @Override
    public void onInputPhone(String phone) {
        showAuthFragment(phone);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MobiApplication.get(this).getComponentContainer().releaseAuthComponent();
        }
        super.onDestroy();
    }
}

