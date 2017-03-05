package mobi.tarantino.stub.auto.feature.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.helper.ViewHelper;


public class InputPhoneFragment extends Fragment {
    private static final String SCREEN_NAME = "input_phone_screen";
    // UI references.
    @BindView(R.id.phone)
    AutoCompleteTextView phoneView;

    @Inject
    IntentStarter intentStarter;

    @Inject
    InputPhoneListener listener;
    @BindView(R.id.agreement_textView)
    TextView agreementTextView;
    @BindView(R.id.why_needed_phone_textView)
    TextView whyNeededPhoneTextView;
    private AnalyticReporter analyticReporter;
    private MenuItem mSendMenuItem;

    private Unbinder unbinder;

    public static Fragment newInstance() {
        return new InputPhoneFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);

        mSendMenuItem = menu.findItem(R.id.menu_send).setVisible(false);

        setMenuVisibility();

        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_phone, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            attemptLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up the login form.
        getActivity().setTitle(R.string.title_phone);
        unbinder = ButterKnife.bind(this, view);
        initAuthComponent();
        initAnalyticReporter();

        initAgreementTextView();
        initPhoneView();
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(getContext())
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    private void initPhoneView() {
        phoneView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* handle action here */
                            handled = true;
                            attemptLogin();
                        }
                        return handled;
                    }
                });
        phoneView.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            @Override
            public synchronized void afterTextChanged(Editable s) {
                if (s.length() > 0 && !s.toString().startsWith("+")) s.insert(0, "+");
                if (s.length() > 1 && !s.toString().startsWith("+7")) s.insert(1, "7");

                setMenuVisibility();

                super.afterTextChanged(s);
            }
        });
    }

    private void initAgreementTextView() {
        Spannable spannable = new SpannableString(agreementTextView.getText());
        spannable.setSpan(new ForegroundColorSpan(
                        ResourcesCompat.getColor(getResources(), R.color.yellowish_orange,
                                getActivity().getTheme())),
                0, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreementTextView.setText(spannable);
    }

    private void setMenuVisibility() {
        if (mSendMenuItem != null) {
            if (phoneView.getText().toString().replaceAll("[^\\d]", "").length() >= 11) {
                mSendMenuItem.setVisible(true);
            } else {
                mSendMenuItem.setVisible(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        analyticReporter.openScreen(Reporter.SCREEN_INPUT_PHONE);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        phoneView.setError(null);

        // Store values at the time of the login attempt.
        String phoneNumber = phoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(phoneNumber) && !isPhoneValid(phoneNumber)) {
            phoneView.setError(getString(R.string.error_invalid_phone));
            focusView = phoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            ViewHelper.hideKeyboard(phoneView, getContext());
            listener.onInputPhone(phoneNumber);
            analyticReporter.phoneNextButtonClick(Reporter.SCREEN_INPUT_PHONE);
        }
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() > 4;
    }

    private void initAuthComponent() {
        MobiApplication.get(getContext())
                .getComponentContainer()
                .getAuthComponent((AuthActivity) getActivity())
                .inject(this);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.agreement_textView)
    public void agreementTextClick() {
        intentStarter.openInBrowser(getString(R.string.offer_url));
        analyticReporter.offerClick(Reporter.SCREEN_INPUT_PHONE);
    }

    @OnClick(R.id.why_needed_phone_textView)
    public void whyNeededPhoneTextClick() {
        Toast.makeText(getContext(), getString(R.string.why_needed_phone), Toast.LENGTH_LONG)
                .show();
        analyticReporter.whatPhoneNumberClick(Reporter.SCREEN_INPUT_PHONE);
    }

    public interface InputPhoneListener {

        void onInputPhone(String phone);
    }
}
