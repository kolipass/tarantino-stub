package mobi.tarantino.stub.auto.feature.dashboard.driverAssistance;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardComponent;
import mobi.tarantino.stub.auto.model.additionalData.pojo.DriverAssistanceInfo;
import mobi.tarantino.stub.auto.mvp.LcePullToRefreshFragment;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;
import rx.functions.Action1;

/**

 */

public class DriverAssistanceFragment extends LcePullToRefreshFragment<DriverAssistanceView,
        DriverAssistancePresenter>
        implements DriverAssistanceView, ErrorHandler.ErrorHandlerListener {

    @BindView(R.id.progress)
    DotProgressBar progress;

    @BindView(R.id.error_container)
    ViewGroup errorContainer;

    @BindView(R.id.retry_button)
    Button retryButton;

    @BindView(R.id.error_textView)
    TextView errorTextView;

    @BindView(R.id.contentView)
    SwipeRefreshLayout contentView;

    @BindView(R.id.phones_recyclerView)
    RecyclerView phonesRecyclerView;

    private DriverAssistanceInfoAdapter adapter;
    private DashBoardComponent dashBoardComponent;
    private AnalyticReporter analyticReporter;
    private Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_driver_assistance;
    }

    @Override
    protected void injectDependencies() {
        dashBoardComponent = MobiApplication.get(getContext())
                .getComponentContainer()
                .getDashBoardComponent((DashBoardActivity) getContext());
        dashBoardComponent.inject(this);
        initAnalyticReporter();
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(getContext())
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRecyclerView() {
        adapter = new DriverAssistanceInfoAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        phonesRecyclerView.setLayoutManager(layoutManager);
        phonesRecyclerView.setAdapter(adapter);
    }

    @NonNull
    @Override
    public DriverAssistancePresenter createPresenter() {
        return dashBoardComponent.driverAssistancePresenter();
    }


    @Override
    public ViewState createViewState() {
        return new CastedArrayListLceViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        new RxPermissions(getActivity())
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        loadData(false);
                    }
                });
    }

    @Override
    public void setData(List<DriverAssistanceInfo> data) {
        getRefreshLayout().setVisibility(View.VISIBLE);
        adapter.setData(data);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        getRefreshLayout().setVisibility(View.VISIBLE);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.load(pullToRefresh);
    }

    @Override
    public View getErrorView() {
        return errorContainer;
    }

    @Override
    public TextView getErrorTextView() {
        return errorTextView;
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return contentView;
    }

    @Override
    public View getLoadingView() {
        return progress;
    }

    @Override
    public CastedArrayListLceViewState<List<DriverAssistanceInfo>,
            DriverAssistanceView> getViewState() {
        return
                (CastedArrayListLceViewState<List<DriverAssistanceInfo>, DriverAssistanceView>)
                        super
                                .getViewState();
    }

    @OnClick(R.id.retry_button)
    public void onRetryButtonClick() {
        loadData(false);
    }

    @Override
    public void onHandleErrorMessage(String errorMessage) {
        analyticReporter.commonError(AnalyticReporter.SCREEN_DRIVER_ASSISTANCE, errorMessage);
        errorTextView.setText(errorMessage);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
