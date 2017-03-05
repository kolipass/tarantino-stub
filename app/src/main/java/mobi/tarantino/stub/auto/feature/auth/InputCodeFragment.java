package mobi.tarantino.stub.auto.feature.auth;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.model.auth.HttpApiException;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthCodeMobiApiAnswer;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthTokenMobiApiAnswer;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.HttpApiExceptionStrategy;
import mobi.tarantino.stub.auto.sms.SmsInterceptor;
import rx.functions.Action1;

import static mobi.tarantino.stub.auto.Consts.Key.PHONE;

/**

 */

public class InputCodeFragment extends MvpViewStateFragment<AuthView, AuthPresenter> implements
        AuthView, SmsInterceptor.SmsCodeListener, ErrorHandler.ErrorHandlerListener {

    @NonNull
    @BindView(R.id.code)
    PinEntryEditText codeView;

    @NonNull
    @BindView(R.id.send_sms_to_textView)
    TextView phoneTextView;

    @NonNull
    @BindView(R.id.progress)
    ViewGroup progress;

    @NonNull
    @BindView(R.id.info_textView)
    TextView infoTextView;

    @NonNull
    @BindView(R.id.phone_login_form)
    ViewGroup loginForm;

    @NonNull
    @BindView(R.id.send_sms_again_textView)
    TextView sendSmsAgainTextView;

    @NonNull
    @BindView(R.id.send_sms_again_button)
    Button sendSmsAgainButton;

    @Inject
    AuthSuccessListener authSuccessListener;
    @Inject
    SmsSendTimerPresenter smsSendTimerPresenter;
    @Inject
    ErrorHandler errorHandler;

    private AnalyticReporter analyticReporter;
    @NonNull
    private String phone;
    @NonNull
    private AuthComponent authComponent;
    private Unbinder unbinder;
    private AuthCodeMobiApiAnswer phoneAnswer;
    private AuthTokenMobiApiAnswer tokenAnswer;
    private SmsInterceptor smsInterceptor;
    private IntentFilter smsInterceptIntentFilter;

    @NonNull
    public static InputCodeFragment newInstance(String phone) {
        Bundle bundle = new Bundle();
        bundle.putString(Consts.Key.PHONE, phone);
        InputCodeFragment fragment = new InputCodeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initAuthComponent();
        initAnalyticReporter();
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initActivityUI();
        initCodeView();
        initPhoneTextView();

        errorHandler.setErrorHandlerListener(this);
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(getContext())
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    private void initSmsInterceptor() {
        new RxPermissions(getActivity())
                .request(Manifest.permission.RECEIVE_SMS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        presenter.sendPhone(phone);
                        smsSendTimerPresenter.init();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        analyticReporter.openScreen(Reporter.SCREEN_INPUT_SMS_CODE);
        if (new RxPermissions(getActivity()).isGranted(Manifest.permission.RECEIVE_SMS)) {
            smsInterceptIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            smsInterceptor = new SmsInterceptor();
            smsInterceptor.setSmsCodeListener(InputCodeFragment.this);
            getActivity().registerReceiver(smsInterceptor, smsInterceptIntentFilter);
        }
        final InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService
                (Context.INPUT_METHOD_SERVICE);
        codeView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(@NonNull View view, boolean hasFocus) {
                if (view.getId() == R.id.code && !hasFocus) {
                    keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (smsInterceptor != null) {
            getActivity().unregisterReceiver(smsInterceptor);
        }
    }

    private void initActivityUI() {
        getActivity().setTitle(R.string.title_apply_code);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initPhoneTextView() {
        phone = getArguments().getString(PHONE);
        phoneTextView.setText(
                String.format(
                        Locale.getDefault(),
                        getString(R.string.label_send_sms_to),
                        phone
                ));
    }

    private void initCodeView() {
        codeView.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                authorize(codeView.getText().toString());
                codeView.setText(null);
            }
        });
        codeView.setSingleLine();
        codeView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (codeView.getText().length() < 6) {
                    Toast.makeText(getActivity(), R.string.enter_full_code, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    authorize(codeView.getText().toString());
                }
                return true;
            }
        });
    }

    private void authorize(String smsCode) {
        presenter.sendSmsCode(phoneAnswer.getCode(), smsCode);
    }

    private void initAuthComponent() {
        authComponent = MobiApplication.get(getContext())
                .getComponentContainer()
                .getAuthComponent((AuthActivity) getActivity());
        authComponent.inject(this);
        errorHandler.registerStrategy(HttpApiException.class, new HttpApiExceptionStrategy() {
            @Override
            protected boolean isCritical(Throwable e) {
                return getErrorCode(e) != 401 && super.isCritical(e);
            }
        });
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new AuthViewState();
    }

    @NonNull
    @Override
    public AuthViewState getViewState() {
        return (AuthViewState) super.getViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        initSmsInterceptor();
    }

    @Override
    public void onDetach() {
        smsSendTimerPresenter.detachView(false);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        codeView.setOnEditorActionListener(null);
        unbinder.unbind();
        super.onDestroyView();
    }

    @NonNull
    @Override
    public AuthPresenter createPresenter() {
        smsSendTimerPresenter.attachView(this);
        return authComponent.provideAuthPresenter();
    }

    @Override
    public void onSendPhoneSuccess(AuthCodeMobiApiAnswer authCodeMobiApiAnswer) {
        getViewState().setStatePhoneSendSuccess(authCodeMobiApiAnswer);
        this.phoneAnswer = authCodeMobiApiAnswer;
        progress.setVisibility(View.GONE);
        showCodeView();
    }

    private void showCodeView() {
        loginForm.setVisibility(View.VISIBLE);

        final InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService
                (Context.INPUT_METHOD_SERVICE);
        codeView.requestFocus();

        codeView.postDelayed(new Runnable() {
            @Override
            public void run() {
                keyboard.showSoftInput(codeView, 0);
            }
        }, 200);
    }

    @Override
    public void onSendPhoneFailed(@NonNull Throwable e) {
        getViewState().setStatePhoneSendFailed();
        errorHandler.handleError(e);
        analyticReporter.errorEnterPhone(Reporter.SCREEN_INPUT_SMS_CODE, e.getMessage());
        getActivity().onBackPressed();
    }

    @Override
    public void onSendPhoneProgress() {
        getViewState().setStatePhoneSendProgress();
        progress.setVisibility(View.VISIBLE);
        loginForm.setVisibility(View.GONE);
        infoTextView.setText(R.string.auth_request);
    }

    @Override
    public void onSendCodeSuccess(AuthTokenMobiApiAnswer authTokenMobiApiAnswer) {
        analyticReporter.authSuccess(Reporter.SCREEN_INPUT_SMS_CODE);
        getViewState().setStateSendCodeSuccess(authTokenMobiApiAnswer);
        this.tokenAnswer = authTokenMobiApiAnswer;
        authSuccessListener.onAuthSuccess();

    }

    @Override
    public void onSendCodeFailed(@NonNull Throwable e) {
        getViewState().setStateSendCodeFailed();
        errorHandler.handleError(e);
        showCodeView();
        progress.setVisibility(View.GONE);
        analyticReporter.errorEnterCode(Reporter.SCREEN_INPUT_SMS_CODE, e.getMessage());
        analyticReporter.authFailed(Reporter.SCREEN_INPUT_SMS_CODE, e.getMessage());
    }

    @Override
    public void onSendCodeProgress() {
        getViewState().setStateSendCodeProgress();
        progress.setVisibility(View.VISIBLE);
        loginForm.setVisibility(View.GONE);
        infoTextView.setText(R.string.auth_progress);
    }

    @Override
    public void onTimerComplete() {
        sendSmsAgainButton.setVisibility(View.VISIBLE);
        sendSmsAgainTextView.setVisibility(View.GONE);
        sendSmsAgainTextView.setText(getString(R.string.send_sms_timer_pattern, 30));
    }

    @Override
    public void onTimerStart() {
        sendSmsAgainTextView.setVisibility(View.VISIBLE);
        sendSmsAgainButton.setVisibility(View.GONE);
    }

    @Override
    public void onTimerNext(long seconds) {
        sendSmsAgainTextView.setText(getString(R.string.send_sms_timer_pattern, seconds));
    }

    @Override
    public void onTimerError() {
        sendSmsAgainTextView.setVisibility(View.GONE);
    }

    @Override
    public void onSendLimitExceeded() {
        Toast.makeText(getActivity(), R.string.send_code_limit_exceeded, Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.menu_send).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onSmsCodeIntercept(String smsCode) {
        authorize(smsCode);
    }

    @OnClick(R.id.send_sms_again_button)
    public void onSendSmsAgainButtonClick() {
        smsSendTimerPresenter.init();
        getPresenter().sendPhone(phone);
        analyticReporter.retrySendCodeClick(Reporter.SCREEN_INPUT_SMS_CODE);
    }

    @Override
    public void onHandleErrorMessage(String errorMessage) {

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public interface AuthSuccessListener {
        void onAuthSuccess();
    }
}
