package mobi.tarantino.stub.auto.feature.auth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import mobi.tarantino.stub.auto.di.ImmediateSchedulersModule;
import mobi.tarantino.stub.auto.model.auth.AuthModel;
import mobi.tarantino.stub.auto.model.auth.MobiAuthApi;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**

 */
public class SmsSendTimerPresenterTest {

    private AuthView view;
    private MobiAuthApi api;
    private SmsSendTimerPresenter presenter;

    @Before
    public void setUp() {
        view = mock(AuthView.class);
        api = mock(MobiAuthApi.class);
        AuthModel model = new AuthModel(api);
        model.setSchedulerProvider(new ImmediateSchedulersModule().providesSchedulers());
        presenter = new SmsSendTimerPresenter(model);
        presenter.setLimit(2);
        presenter.attachView(view);
    }

    @Test
    public void timer2secTest() {
        presenter.init();
        verify(view).onTimerStart();
        verify(view, VerificationModeFactory.times(2)).onTimerNext(anyLong());
        verify(view).onTimerComplete();
    }
}