package mobi.tarantino.stub.auto.feature.auth;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.model.auth.AuthModel;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthCodeMobiApiAnswer;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthTokenMobiApiAnswer;
import mobi.tarantino.stub.auto.mvp.MvpLceMultiRxPresenter;
import rx.Subscriber;

/**

 */

public class AuthPresenter extends MvpLceMultiRxPresenter<AuthView> {

    private AuthModel model;
    private IPreferencesManager preferencesManager;
    private String phone;
    private int sendCount = 0;

    @Inject
    public AuthPresenter(AuthModel model, IPreferencesManager preferencesManager) {
        super();
        this.model = model;
        this.preferencesManager = preferencesManager;
    }

    public void sendSmsCode(String secretCode, String smsCode) {
        unsubscribe();
        if (getView() != null && isViewAttached()) {
            sendCodeProgress();
            addSubscription(model.sendCode(secretCode, smsCode).subscribe(getSendCodeSubscriber()));
        }
    }

    public void sendPhone(String phone) {
        unsubscribe();
        setPhone(phone);
        if (getView() != null && isViewAttached()) {
            sendPhoneProgress();

            this.phone = phone.replaceAll("[^\\d]", "");
            addSubscription(model.sendPhone(this.phone)
                    .subscribe(getSendPhoneSubscriber()));
        }
    }

    public Subscriber<AuthTokenMobiApiAnswer> getSendCodeSubscriber() {
        return new Subscriber<AuthTokenMobiApiAnswer>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                sendCount++;
                if (sendCount >= 3) {
                    sendLimitExceeded();
                } else {
                    sendCodeFailed(e);
                }
            }

            @Override
            public void onNext(AuthTokenMobiApiAnswer authTokenMobiApiAnswer) {
                sendCodeSuccess(authTokenMobiApiAnswer);
            }
        };
    }

    public Subscriber<AuthCodeMobiApiAnswer> getSendPhoneSubscriber() {
        return new Subscriber<AuthCodeMobiApiAnswer>() {
            @Override
            public void onCompleted() {
                //do nothing
            }

            @Override
            public void onError(Throwable e) {
                sendPhoneFailed(e);
            }

            @Override
            public void onNext(AuthCodeMobiApiAnswer authCodeMobiApiAnswer) {
                sendPhoneSuccess(authCodeMobiApiAnswer);
            }
        };
    }

    private void sendLimitExceeded() {
        sendCount = 0;
        if (getView() != null && isViewAttached()) {
            getView().onSendLimitExceeded();
        }
    }

    private void sendPhoneFailed(Throwable e) {
        if (getView() != null && isViewAttached()) {
            getView().onSendPhoneFailed(e);
        }
    }

    private void sendPhoneSuccess(AuthCodeMobiApiAnswer authCodeMobiApiAnswer) {
        if (getView() != null && isViewAttached()) {
            getView().onSendPhoneSuccess(authCodeMobiApiAnswer);
        }
    }

    private void sendPhoneProgress() {
        if (getView() != null && isViewAttached()) {
            getView().onSendPhoneProgress();
        }
    }

    private void sendCodeFailed(Throwable e) {
        if (getView() != null && isViewAttached()) {
            getView().onSendCodeFailed(e);
        }
    }

    private void sendCodeSuccess(AuthTokenMobiApiAnswer authTokenMobiApiAnswer) {
        preferencesManager.setToken(authTokenMobiApiAnswer.getAccessToken());
        preferencesManager.setPhone(phone);

        if (getView() != null && isViewAttached()) {
            getView().onSendCodeSuccess(authTokenMobiApiAnswer);
        }
    }

    private void sendCodeProgress() {
        if (getView() != null && isViewAttached()) {
            getView().onSendCodeProgress();
        }
    }

    private void setPhone(String phone) {
        this.phone = phone;
    }
}
