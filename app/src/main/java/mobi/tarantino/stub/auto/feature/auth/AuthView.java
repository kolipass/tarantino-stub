package mobi.tarantino.stub.auto.feature.auth;

import com.hannesdorfmann.mosby.mvp.MvpView;

import mobi.tarantino.stub.auto.model.auth.pojo.AuthCodeMobiApiAnswer;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthTokenMobiApiAnswer;

/**

 */
public interface AuthView extends MvpView {
    void onSendPhoneSuccess(AuthCodeMobiApiAnswer authCodeMobiApiAnswer);

    void onSendPhoneFailed(Throwable e);

    void onSendPhoneProgress();

    void onSendCodeSuccess(AuthTokenMobiApiAnswer authTokenMobiApiAnswer);

    void onSendCodeFailed(Throwable e);

    void onSendCodeProgress();

    void onTimerComplete();

    void onTimerStart();

    void onTimerNext(long seconds);

    void onTimerError();

    void onSendLimitExceeded();
}
