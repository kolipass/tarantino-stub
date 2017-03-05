package mobi.tarantino.stub.auto.feature.dashboard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.layout.MvpViewStateFrameLayout;

import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.di.BaseComponent;
import mobi.tarantino.stub.auto.di.DependencyComponentManager;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;

/**

 */
public abstract class AbstractCardLayout<D, V extends MvpLceView<D>, P extends MvpPresenter<V>>
        extends MvpViewStateFrameLayout<V, P>
        implements MvpLceView<D>, ErrorHandler.ErrorHandlerListener {

    protected AnalyticReporter analyticReporter;
    private ErrorHandler errorHandler;

    public AbstractCardLayout(Context context) {
        super(context);
    }

    public AbstractCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbstractCardLayout(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            injectDependencies();
        }
    }

    private void injectDependencies() {
        DependencyComponentManager componentContainer = MobiApplication.get(getContext())
                .getComponentContainer();
        BaseComponent baseComponent = componentContainer.getBaseComponent();
        errorHandler = baseComponent.errorHandler();
        errorHandler.setErrorHandlerListener(this);

        analyticReporter = componentContainer.getAnalyticComponent().provideAnalyticReporter();
    }

    @Override
    public void showContent() {
        showCardContent();
        hideErrorContent();
        hideProgressContent();
        setInfoText();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        hideCardContent();
        hideProgressContent();
        showErrorContent();
        setRetryViewListener();
        setErrorText(e);
    }

    private void setRetryViewListener() {
        View v = getRetryView();
        if (v != null) {
            getRetryView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRetryClick();
                }
            });
        }
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        if (!pullToRefresh) {
            hideCardContent();
            hideErrorContent();
            showProgressContent();
            setOnLoadingText();
        } else {
            hideErrorContent();
            showProgressContent();
            setOnLoadingText();
        }
    }

    private void setOnLoadingText() {
        String loadingText = getLoadingText();
        if (TextUtils.isEmpty(loadingText)) return;
        TextView loadingTextView = getLoadingTextView();
        if (loadingTextView != null) loadingTextView.setText(loadingText);
    }

    protected abstract String getLoadingText();

    @Nullable
    protected abstract TextView getLoadingTextView();

    protected void setInfoText() {
        String infoText = getInfoText();
        if (TextUtils.isEmpty(infoText)) return;
        TextView infoTextView = getInfoTextView();
        if (infoTextView != null) infoTextView.setText(infoText);
    }

    @Nullable
    public abstract String getErrorText();

    private void setErrorText(Throwable e) {
        errorHandler.handleError(e);
    }

    @Nullable
    public abstract String getInfoText();

    public abstract void onRetryClick();

    @Nullable
    public abstract View getCardContentView();

    @Nullable
    public abstract View getErrorContentView();

    @Nullable
    public abstract View getProgressContentView();

    @Nullable
    public abstract View getRetryView();

    @Nullable
    public abstract TextView getErrorTextView();

    @Nullable
    public abstract TextView getInfoTextView();

    public void showCardContent() {
        View v = getCardContentView();
        if (v != null) v.setVisibility(VISIBLE);
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void hideCardContent() {
        View v = getCardContentView();
        if (v != null) v.setVisibility(GONE);
    }

    public void showErrorContent() {
        View v = getErrorContentView();
        if (v != null) v.setVisibility(VISIBLE);
    }

    public void hideErrorContent() {
        View v = getErrorContentView();
        if (v != null) v.setVisibility(GONE);
    }

    public void showProgressContent() {
        View v = getProgressContentView();
        if (v != null) v.setVisibility(VISIBLE);
    }

    public void hideProgressContent() {
        View v = getProgressContentView();
        if (v != null) v.setVisibility(GONE);
    }

    @Override
    public void onHandleErrorMessage(String errorMessage) {

        analyticReporter.commonError(AnalyticReporter.SCREEN_SERVICES, errorMessage);
        String errorText = getErrorText();
        TextView errorTextView = getErrorTextView();
        if (TextUtils.isEmpty(errorText)) {
            if (errorTextView != null) errorTextView.setText(errorMessage);
        } else {
            if (errorTextView != null) errorTextView.setText(errorText);
        }
    }
}
