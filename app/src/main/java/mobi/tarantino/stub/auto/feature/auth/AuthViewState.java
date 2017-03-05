package mobi.tarantino.stub.auto.feature.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import mobi.tarantino.stub.auto.model.auth.pojo.AuthCodeMobiApiAnswer;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthTokenMobiApiAnswer;

import static mobi.tarantino.stub.auto.Consts.Key.SEND_PHONE_ANSWER;
import static mobi.tarantino.stub.auto.Consts.Key.SEND_SMS_CODE_ANSWER;
import static mobi.tarantino.stub.auto.Consts.Key.VIEW_STATE;

/**

 */

public class AuthViewState implements RestorableViewState<AuthView> {

    private AuthViewState.AuthState state;
    private AuthCodeMobiApiAnswer phoneAnswer;
    private AuthTokenMobiApiAnswer tokenAnswer;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putSerializable(VIEW_STATE, state);
        out.putParcelable(SEND_PHONE_ANSWER, phoneAnswer);
        out.putParcelable(SEND_SMS_CODE_ANSWER, tokenAnswer);
    }

    @Override
    public RestorableViewState restoreInstanceState(Bundle in) {
        state = (AuthState) in.getSerializable(VIEW_STATE);
        phoneAnswer = in.getParcelable(SEND_PHONE_ANSWER);
        tokenAnswer = in.getParcelable(SEND_SMS_CODE_ANSWER);
        return this;
    }

    @Override
    public void apply(AuthView view, boolean retained) {
        switch (state) {
            case SEND_PHONE_PROGRESS:
                view.onSendPhoneProgress();
                break;
            case SEND_PHONE_SUCCESS:
                view.onSendPhoneSuccess(phoneAnswer);
                break;
            case SEND_PHONE_FAILED:
            case SEND_CODE_FAILED:
//                view.onSendPhoneFailed(null);
//                view.onSendCodeFailed(null);
                break;
            case SEND_CODE_PROGRESS:
                view.onSendCodeProgress();
                break;
            case SEND_CODE_SUCCESS:
                view.onSendCodeSuccess(tokenAnswer);
                break;
        }
    }

    public void setStatePhoneSendProgress() {
        state = AuthState.SEND_PHONE_PROGRESS;
    }

    public void setStatePhoneSendSuccess(AuthCodeMobiApiAnswer answer) {
        state = AuthState.SEND_PHONE_SUCCESS;
        phoneAnswer = answer;
    }

    public void setStatePhoneSendFailed() {
        state = AuthState.SEND_PHONE_FAILED;
    }

    public void setStateSendCodeProgress() {
        state = AuthState.SEND_CODE_PROGRESS;
    }

    public void setStateSendCodeSuccess(AuthTokenMobiApiAnswer answer) {
        state = AuthState.SEND_CODE_SUCCESS;
        tokenAnswer = answer;
    }

    public void setStateSendCodeFailed() {
        state = AuthState.SEND_CODE_FAILED;
    }

    enum AuthState {
        SEND_PHONE_PROGRESS,
        SEND_PHONE_SUCCESS,
        SEND_PHONE_FAILED,

        SEND_CODE_PROGRESS,
        SEND_CODE_SUCCESS,
        SEND_CODE_FAILED
    }
}
