package mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.layout.ViewStateSavedState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.feature.dashboard.AbstractCardLayout;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardComponent;
import mobi.tarantino.stub.auto.feature.dashboard.services.OnBeUserVieweble;
import mobi.tarantino.stub.auto.helper.AnimationHelper;
import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices;

import static mobi.tarantino.stub.auto.analytics.Reporter.CATEGORY_FUEL_INFO;

/**

 */
public class FuelInfoCardLayout extends AbstractCardLayout<FuelPrices, FuelInfoCardView,
        FuelInfoCardPresenter>
        implements FuelInfoCardView, TabLayout.OnTabSelectedListener, OnBeUserVieweble {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.progress_fuel_card)
    View progressBar;
    @BindView(R.id.card_content)
    ViewGroup cardContent;
    @BindView(R.id.info_textView)
    TextView infoTextView;
    @BindView(R.id.error_textView)
    TextView errorTextView;
    @BindView(R.id.error_container)
    ViewGroup errorContainer;
    @BindView(R.id.retry_button)
    Button retryButton;
    @BindView(R.id.fuel_info_recyclerView)
    RecyclerView fuelInfoRecyclerView;
    @BindView(R.id.update_button)
    ImageView updateButton;
    private FuelPrices data;
    private FuelInfoListAdapter adapter;
    private DashBoardComponent dashBoardComponent;

    private List<TabLayout.Tab> tabList;

    public FuelInfoCardLayout(Context context) {
        super(context);
    }

    public FuelInfoCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FuelInfoCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public FuelInfoCardLayout(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.bind(this, this);
            initTabLayout();
            adapter = new FuelInfoListAdapter(getContext());
            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            fuelInfoRecyclerView.setLayoutManager(linearLayoutManager);
            fuelInfoRecyclerView.setAdapter(adapter);
        }
    }

    private void initTabLayout() {
        tabList = new ArrayList<>();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabList.add(tabLayout.getTabAt(i));
        }
        tabLayout.addOnTabSelectedListener(this);
    }


    @NonNull
    @Override
    public ViewState<FuelInfoCardView> createViewState() {
        return new FuelInfoCardViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        loadData(false);
    }

    @NonNull
    @Override
    public FuelInfoCardPresenter createPresenter() {

        if (isInEditMode()) return new FuelInfoCardPresenter();
        dashBoardComponent = MobiApplication.get(getContext())
                .getComponentContainer()
                .getDashBoardComponent((DashBoardActivity) getContext());
        return dashBoardComponent
                .fuelInfoCardPresenter();
    }

    @Override
    public FuelInfoCardViewState getViewState() {
        return (FuelInfoCardViewState) super.getViewState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        ViewStateSavedState viewStateSavedState = (ViewStateSavedState) state;
        setViewState(viewStateSavedState.getMosbyViewState());
        getViewState().apply(this, true);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        getViewState().setStateShowLoading(false);

    }

    @Override
    protected String getLoadingText() {
        return getContext().getString(R.string.data_loading);
    }

    @Override
    protected TextView getLoadingTextView() {
        return infoTextView;
    }

    @Override
    public String getErrorText() {
        return null;
    }

    @Override
    public String getInfoText() {
        return getViewState().isDefaultCity() ?
                getContext().getString(R.string.fuel_in_moscow) :
                getContext().getString(R.string.fuel_in_russia);
    }

    @Override
    public void setLocation(@Nullable String s) {
        getViewState().setDefaultCity(s != null);
    }

    @Override
    public void onRetryClick() {
        loadData(false);
    }

    @Override
    public View getCardContentView() {
        return cardContent;
    }

    @Override
    public View getErrorContentView() {
        return errorContainer;
    }

    @Override
    public View getProgressContentView() {
        return progressBar;
    }

    @Override
    public View getRetryView() {
        return retryButton;
    }

    @Override
    public TextView getErrorTextView() {
        return errorTextView;
    }

    @Override
    public TextView getInfoTextView() {
        return infoTextView;
    }

    @Override
    public void showContent() {
        super.showContent();
        getViewState().setStateShowContent(data);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        getViewState().setStateShowError(e, pullToRefresh);
    }

    @Override
    public void setData(FuelPrices data) {
        this.data = data;
        if (data != null) {
            adapter.setData(data.getAi92());
        }
        enableUpdateButton();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (!isInEditMode()) {
            disableUpdateButton();
            presenter.loadData(pullToRefresh);
        }
    }


    @Override
    public void setTabState(int position) {
        tabList.get(position).select();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        getViewState().saveTabState(tab.getPosition());
        FuelPrices.FuelType fuelType = getFuelType(tab.getPosition());
        List<FuelPrices.FuelInfo> fuelInfo = data.getFuelInfo(fuelType);
        adapter.setData(fuelInfo);

        analyticReporter.widgetClickEvent(Reporter.SCREEN_SERVICES, CATEGORY_FUEL_INFO, fuelType
                .toString());

    }

    private FuelPrices.FuelType getFuelType(int position) {
        switch (position) {
            case 0:
                return FuelPrices.FuelType.AI_92;
            case 1:
                return FuelPrices.FuelType.AI_95;
            case 2:
                return FuelPrices.FuelType.AI_98;
            case 3:
                return FuelPrices.FuelType.DIESEL;
            default:
                return FuelPrices.FuelType.AI_92;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick(R.id.update_button)
    public void onUpdateButtonClick() {
        loadData(true);
        analyticReporter.refreshWidgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_FUEL_INFO);
    }

    private void enableUpdateButton() {
        updateButton.setEnabled(true);
        AnimationHelper.rotateWithAlpha(500, 90f, 0f, 0f, 1f, updateButton);
    }

    private void disableUpdateButton() {
        updateButton.setEnabled(false);
        AnimationHelper.rotateWithAlpha(500, 0f, 90f, 1f, 0f, updateButton);
    }


    @Override
    public void onVieweble() {
        analyticReporter.widgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_FUEL_INFO);
    }
}
