package mobi.tarantino.stub.auto.feature.dashboard.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.mock.MockContext;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.ParcelableDataLceViewState;

import org.junit.Before;
import org.junit.Test;

import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.feature.dashboard.AbstractCardLayout;
import mobi.tarantino.stub.auto.mvp.MvpLceRxPresenter;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**

 */
public class AbstractCardLayoutTest {

    AbstractCardLayout abstractCardLayout;
    MockContext context;

    @Before
    public void setUp() {
        context = new MockContext();
        abstractCardLayout = spy(new AbstractCardLayout(mock(Context.class)) {

            @NonNull
            @Override
            protected String getLoadingText() {
                return "";
            }

            @Override
            protected TextView getLoadingTextView() {
                return mock(TextView.class);
            }

            @NonNull
            @Override
            public String getErrorText() {
                return "";
            }

            @NonNull
            @Override
            public String getInfoText() {
                return "";
            }

            @Override
            public void onRetryClick() {
                //nothing
            }

            @Override
            public View getCardContentView() {
                return mock(View.class);
            }

            @Override
            public View getErrorContentView() {
                return mock(View.class);
            }

            @Override
            public View getProgressContentView() {
                return mock(View.class);
            }

            @Override
            public View getRetryView() {
                return mock(View.class);
            }

            @Override
            public TextView getErrorTextView() {
                return mock(TextView.class);
            }

            @Override
            public TextView getInfoTextView() {
                return mock(TextView.class);
            }

            @NonNull
            @Override
            public ViewState createViewState() {
                return new ParcelableDataLceViewState();
            }

            @Override
            public void onNewViewStateInstance() {
                //nothing
            }

            @NonNull
            @Override
            public MvpPresenter createPresenter() {
                return mock(MvpLceRxPresenter.class);
            }

            @Override
            public void setData(Object data) {
                //nothing
            }

            @Override
            public void loadData(boolean pullToRefresh) {
                //nothing
            }
        });
        abstractCardLayout.setErrorHandler(new ErrorHandler(context, mock(IntentStarter.class)));
    }

    @Test
    public void showErrorTest() {
        abstractCardLayout.showError(new Throwable(), false);
        verify(abstractCardLayout).showErrorContent();
        verify(abstractCardLayout).hideCardContent();
        verify(abstractCardLayout).hideProgressContent();
    }

    @Test
    public void showProgressTest() {
        abstractCardLayout.showLoading(false);
        verify(abstractCardLayout).hideErrorContent();
        verify(abstractCardLayout).hideCardContent();
        verify(abstractCardLayout).showProgressContent();
    }

    @Test
    public void showContentTest() {
        abstractCardLayout.showContent();
        verify(abstractCardLayout).showCardContent();
        verify(abstractCardLayout).hideErrorContent();
        verify(abstractCardLayout).hideProgressContent();
    }

}