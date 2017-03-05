package mobi.tarantino.stub.auto.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;

import javax.inject.Inject;

import butterknife.OnClick;
import butterknife.Optional;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;

/**

 */
public abstract class LcePullToRefreshFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpViewStateFragment<V, P>
        implements SwipeRefreshLayout.OnRefreshListener, ErrorHandler.ErrorHandlerListener {

    @Inject
    Logger logger;
    private ErrorHandler errorHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        injectInternalDependencies();
        injectDependencies();
        super.onViewCreated(view, savedInstanceState);

        getRefreshLayout().setOnRefreshListener(this);
    }

    private void injectInternalDependencies() {
        errorHandler = MobiApplication.get(getContext()).getComponentContainer().getBaseComponent
                ().errorHandler();
        errorHandler.setErrorHandlerListener(this);
    }

    public void onRefresh() {
        loadData(true);
    }

    public void showContent() {
        getLoadingView().setVisibility(View.GONE);
        getRefreshLayout().setVisibility(View.VISIBLE);
        getErrorView().setVisibility(View.GONE);
        getRefreshLayout().setRefreshing(false);
    }

    public void showError(Throwable e, boolean pullToRefresh) {
        getLoadingView().setVisibility(View.GONE);
        getRefreshLayout().setVisibility(View.GONE);
        getErrorView().setVisibility(View.VISIBLE);
        getRefreshLayout().setRefreshing(false);
        errorHandler.handleError(e);
        logger.e(e);
    }

    protected void makeErrorToast(String e) {
        Toast.makeText(getContext(), e, Toast.LENGTH_SHORT).show();
    }

    @Optional
    @OnClick(R.id.retry_button)
    void retryClick() {
        loadData(false);
    }

    public abstract void loadData(boolean pullToRefresh);

    public abstract View getErrorView();

    public abstract TextView getErrorTextView();

    public abstract SwipeRefreshLayout getRefreshLayout();

    public abstract View getLoadingView();

    public void showLoading(boolean pullToRefresh) {
        if (!pullToRefresh) {
            getLoadingView().setVisibility(View.VISIBLE);
            getRefreshLayout().setVisibility(View.GONE);
        }
        getErrorView().setVisibility(View.GONE);
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void injectDependencies();

    @Override
    public void onHandleErrorMessage(String errorMessage) {
        if (getErrorTextView() != null) {
            getErrorTextView().setText(errorMessage);
        } else {
            makeErrorToast(errorMessage);
        }
    }
}
