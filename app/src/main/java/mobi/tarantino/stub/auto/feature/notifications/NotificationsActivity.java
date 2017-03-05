package mobi.tarantino.stub.auto.feature.notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.ParcelableDataLceViewState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.decoration.ToolbarColorizer;
import mobi.tarantino.stub.auto.decoration.ToolbarDecorator;
import mobi.tarantino.stub.auto.decoration.WhiteToolbarColorizer;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard.NotificationDTO;
import mobi.tarantino.stub.auto.helper.ResourcesHelper;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;


public class NotificationsActivity extends MvpViewStateActivity<NotificationsView,
        NotificationsPresenter>
        implements ToolbarDecorator, NotificationsView {

    @BindView(R.id.toolbar_shadow)
    View toolbarShadow;
    @BindView(R.id.events_container)
    ViewGroup eventsContainer;
    @BindView(R.id.fines_container)
    ViewGroup finesContainer;
    @BindView(R.id.laws_container)
    ViewGroup lawsContainer;
    @BindView(R.id.content)
    ViewGroup content;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.error_container)
    ViewGroup errorContainer;
    @BindView(R.id.retry_button)
    Button retryButton;
    @Inject
    ResourcesHelper resourcesHelper;
    @Inject
    IntentStarter intentStarter;
    private Unbinder unbinder;
    private Toolbar toolbar;
    private NotificationDTO data;
    private NotificationsComponent component;

    private View.OnClickListener onFineClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFines();
        }
    };
    private View.OnClickListener onLawClickListener = new ArticleClickListener(ArticleDBO.TYPE_LAW);
    private View.OnClickListener onEventClickListener = new ArticleClickListener(ArticleDBO
            .TYPE_PARTNER_ACTIONS);

    public static Intent createIntent(Context context, NotificationDTO notificationDTO) {
        Intent intent = new Intent(context, NotificationsActivity.class);
        intent.putExtra(Consts.Key.NOTIFICATION_DTO, notificationDTO);
        return intent;
    }

    private void showArticle(ArticleDBO article, String type) {
        intentStarter.showArticle(this, type, article);
    }

    private void openFines() {
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Consts.Notification.CATEGORY_NEW_FINE);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        unbinder = ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initComponent() {
        component = MobiApplication.get(this).getComponentContainer()
                .createNotificationsComponent();
        component.inject(this);
    }

    private void initUI() {
        initEvents();
        initFines();
        initLaws();
    }

    private void initLaws() {
        lawsContainer.removeAllViews();
        for (ArticleDBO article : data.getAllLaws()) {
            View item = getView(article, R.drawable.ic_traffic_laws_grey_36);
            item.setOnClickListener(onLawClickListener);
            lawsContainer.addView(item);
        }
    }

    private void initEvents() {
        eventsContainer.removeAllViews();
        for (ArticleDBO article : data.getAllEvents()) {
            if (article != null) {
                View item = getView(article, R.drawable.ic_event);
                item.setOnClickListener(onEventClickListener);
                eventsContainer.addView(item);
            }
        }
    }

    @NonNull
    private View getView(ArticleDBO article, int icon) {
        View item = getLayoutInflater().inflate(R.layout.notification_item_layout, lawsContainer,
                false);
        ((ImageView) item.findViewById(R.id.notification_imageView))
                .setImageDrawable(resourcesHelper.getVectorDrawable(icon));
        ((TextView) item.findViewById(R.id.title_textView)).setText(article.getTitle());
        item.setTag(article);
        return item;
    }

    private void initFines() {
        finesContainer.removeAllViews();
        int finesCount = data.getFinesCount();
        if (finesCount > 0) {
            View item = getLayoutInflater().inflate(R.layout.notification_item_layout,
                    finesContainer, false);
            ((ImageView) item.findViewById(R.id.notification_imageView))
                    .setImageDrawable(resourcesHelper.getVectorDrawable(R.drawable.ic_new_fine));
            ((TextView) item.findViewById(R.id.title_textView)).setText(
                    getString(R.string.new_fine_notification_title_pattern,
                            getResources().getQuantityString(R.plurals.finesCount, finesCount,
                                    finesCount)));
            item.setOnClickListener(onFineClickListener);
            finesContainer.addView(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        initToolbar();
        return super.onPrepareOptionsMenu(menu);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarShadow.setVisibility(View.GONE);
        }
        applyToolbarColorizer(new WhiteToolbarColorizer(this));
    }

    @Override
    public void applyToolbarColorizer(ToolbarColorizer toolbarColorizer) {
        toolbar.setBackgroundColor(toolbarColorizer.getBackgroundColor());
        toolbar.setTitleTextColor(toolbarColorizer.getTitleColor());
        toolbar.setSubtitleTextColor(toolbarColorizer.getSubTitleColor());

        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(toolbarColorizer
                .getIconColor(), PorterDuff.Mode.SRC_ATOP);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(colorFilter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(toolbarColorizer.getStatusBarColor());
        }

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        if (isFinishing()) {
            MobiApplication.get(this).getComponentContainer().releaseNotificationsComponent();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        MobiApplication.get(this).getComponentContainer().releaseNotificationsComponent();

        super.finish();
    }

    @NonNull
    @Override
    public NotificationsPresenter createPresenter() {
        return component.notificationsPresenter();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        getViewState().setStateShowLoading(pullToRefresh);
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        getViewState().setStateShowContent(data);
        progressBar.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
        initUI();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        getViewState().setStateShowError(e, pullToRefresh);
        progressBar.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(NotificationDTO data) {
        this.data = data;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.load(false);
    }

    @OnClick(R.id.retry_button)
    public void onRetryButtonClick() {
        presenter.load(true);
    }

    @Override
    public ViewState<NotificationsView> createViewState() {
        return new ParcelableDataLceViewState<NotificationDTO, NotificationsView>();
    }

    @Override
    public void onNewViewStateInstance() {
        loadData(false);
    }

    @Override
    public ParcelableDataLceViewState<NotificationDTO, NotificationsView> getViewState() {
        return (ParcelableDataLceViewState<NotificationDTO, NotificationsView>) super
                .getViewState();
    }

    class ArticleClickListener implements View.OnClickListener {
        private String typeLaw;

        ArticleClickListener(String typeLaw) {
            this.typeLaw = typeLaw;
        }

        @Override
        public void onClick(View v) {
            ArticleDBO article = (ArticleDBO) v.getTag();
            showArticle(article, typeLaw);
        }
    }
}
