package mobi.tarantino.stub.auto.feature.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.ButterKnife;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.di.DependencyComponentManager;
import mobi.tarantino.stub.auto.eventbus.RefreshDocumentEvent;
import mobi.tarantino.stub.auto.eventbus.RefreshUnpaidFinesEvent;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;
import mobi.tarantino.stub.auto.mvp.BaseMvpActivity;


public class DashBoardActivity extends BaseMvpActivity<DashBoardView, DashBoardPresenter>
        implements DashBoardView, ArticleListener, ShowAddDocumentListener,
        ShowDriverAssistanseListener, ViewPageContainer {

    @Inject
    DashBoardPresenter presenter;
    @Inject
    IntentStarter starter;
    private DependencyComponentManager componentManager;
    private AnalyticReporter analyticReporter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        RefreshDocumentEvent refreshDocumentEvent = EventBus.getDefault()
                .getStickyEvent(RefreshDocumentEvent.class);

        if (refreshDocumentEvent != null) {
            onRefreshUnpaidCounter(new RefreshUnpaidFinesEvent());
        }
        analyticReporter.openScreen(Reporter.SCREEN_DASH_BOARD);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUnpaidCounter(RefreshUnpaidFinesEvent event) {
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void injectDependencies() {
        componentManager = MobiApplication.get(this)
                .getComponentContainer();
        componentManager.getDashBoardComponent(this).inject(this);
        initAnalyticReporter();
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(this)
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    @NonNull
    @Override
    public DashBoardPresenter createPresenter() {
        presenter.attachView(this);
        return presenter;
    }

    @Override
    public void showArticle(String type, Article article) {
        starter.showArticle(this, type, ArticleDBO.createOrUpdate(new ArticleDBO().setType
                (ArticleDBO.TYPE_LAW), article));
    }

    @Override
    public void showArticles() {
    }

    public void postShowDialog(Fragment dialogFragment) {
        getSupportFragmentManager().beginTransaction()
                .add(dialogFragment, dialogFragment.getClass().getSimpleName())
                .commitAllowingStateLoss();


    }

    @Override
    public void showAddingDriverLicenseScreen() {
//        starter.addDriver(this);
        analyticReporter.addDriverEvent(Reporter.SCREEN_DASH_BOARD);
    }

    @Override
    public void showAddingVehicleRegistrationScreen() {
//        starter.addCar(this);
        analyticReporter.addCarEvent(Reporter.SCREEN_DASH_BOARD);
    }

    @Override
    public void showAddingDocumentDialog() {
        AddDocumentChooser addDocumentChooser = AddDocumentChooser.getInstance();
        addDocumentChooser.show(getSupportFragmentManager(), addDocumentChooser.getClass()
                .getSimpleName());
    }

    @Override
    public void showDriverAssistance() {
//        viewPager.setCurrentItem(STATE_DRIVER_ASSISTANCE);
    }

    @Override
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {

    }

    @Override
    public void removeOnPageChangeListener(ViewPager.OnPageChangeListener listener) {

    }

    @Override
    protected void onDestroy() {
        componentManager.releaseDashBoardComponent();

        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        super.onDestroy();
    }
}
