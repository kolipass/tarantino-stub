package mobi.tarantino.stub.auto.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;

public abstract class BaseMvpViewStateActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpViewStateActivity<V, P> implements ErrorHandler.ErrorHandlerListener {
    protected Unbinder unbinder;
    protected AnalyticReporter analyticReporter;
    @Inject
    ErrorHandler errorHandler;

    protected abstract int getLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        injectDependencies();
        initAnalyticReporter();
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);

        errorHandler.setErrorHandlerListener(this);

    }

    protected abstract void injectDependencies();

    protected void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(this)
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    protected void handleError(Throwable e) {
        if (errorHandler != null) {
            errorHandler.handleError(e);
        }
    }
}
