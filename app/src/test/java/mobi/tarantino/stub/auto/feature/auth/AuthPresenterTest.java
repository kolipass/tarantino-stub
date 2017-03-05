package mobi.tarantino.stub.auto.feature.auth;

import org.junit.Before;
import org.junit.Test;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.model.auth.AuthModel;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthCodeMobiApiAnswer;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthTokenMobiApiAnswer;
import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**

 */
public class AuthPresenterTest {
    private static final String phone = "+70000000000";
    private AuthModel model;
    private AuthView view;
    private IPreferencesManager preferencesManager;
    private AuthPresenter presenter;

    @Before
    public void init() {
        model = mock(AuthModel.class);
        view = mock(AuthView.class);
        preferencesManager = mock(IPreferencesManager.class);
        presenter = new AuthPresenter(model, preferencesManager);
        presenter.attachView(view);
    }

    @Test
    public void sendPhoneSuccessTest() {
        AuthCodeMobiApiAnswer answer = new AuthCodeMobiApiAnswer();
        Observable<AuthCodeMobiApiAnswer> sendPhoneObs = Observable.just(answer);

        when(model.sendPhone(anyString())).thenReturn(sendPhoneObs);

        presenter.sendPhone(phone);
        verify(view).onSendPhoneProgress();
        verify(view).onSendPhoneSuccess(answer);
    }

    @Test
    public void sendCodeSuccessTest() {
        AuthTokenMobiApiAnswer answer = new AuthTokenMobiApiAnswer();
        Observable<AuthTokenMobiApiAnswer> sendCodeObs = Observable.just(answer);

        when(model.sendCode("", "")).thenReturn(sendCodeObs);

        presenter.sendSmsCode("", "");

        verify(view).onSendCodeProgress();
        verify(view).onSendCodeSuccess(answer);


    }
}