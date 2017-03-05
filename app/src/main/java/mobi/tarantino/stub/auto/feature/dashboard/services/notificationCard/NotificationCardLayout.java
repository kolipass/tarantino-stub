package mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.ParcelableDataLceViewState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.feature.dashboard.AbstractCardLayout;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardComponent;
import mobi.tarantino.stub.auto.feature.dashboard.services.OnBeUserVieweble;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

import static mobi.tarantino.stub.auto.analytics.Reporter.CATEGORY_NOTIFICATION;


public class NotificationCardLayout extends AbstractCardLayout<NotificationDTO,
        NotificationCardView, NotificationCardPresenter<NotificationCardView>>
        implements View.OnClickListener, NotificationCardView, OnBeUserVieweble {

    @Nullable
    @BindView(R.id.progress_notification)
    View progressBar;

    @Nullable
    @BindView(R.id.card_content)
    ViewGroup cardContent;

    @Nullable
    @BindView(R.id.retry_button)
    Button retryButton;

    @Nullable
    @BindView(R.id.error_textView)
    TextView errorTextView;

    @Nullable
    @BindView(R.id.notification_title_textView)
    TextView notificationTitleTextView;
    @Nullable
    @BindView(R.id.notification_icon)
    ImageView icon;

    @Nullable
    @BindView(R.id.error_container)
    ViewGroup errorContainer;

    @Inject
    IntentStarter intentStarter;

    private DashBoardComponent dashBoardComponent;
    private NotificationDTO data;

    public NotificationCardLayout(Context context) {
        super(context);
    }

    public NotificationCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NotificationCardLayout(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.bind(this);
            initDashBoardComponent();
        }
        setOnClickListener(this);
    }

    private void initDashBoardComponent() {
        dashBoardComponent =
                MobiApplication
                        .get(getContext())
                        .getComponentContainer()
                        .getDashBoardComponent((DashBoardActivity) getContext());
        dashBoardComponent.inject(this);
    }

    @NonNull
    @Override
    protected String getLoadingText() {
        return getContext().getString(R.string.data_loading);
    }

    @Nullable
    @Override
    protected TextView getLoadingTextView() {
        return notificationTitleTextView;
    }

    @NonNull
    @Override
    public String getErrorText() {
        return getContext().getString(R.string.load_data_error);
    }

    @Nullable
    @Override
    public String getInfoText() {
        return null;
    }

    @Override
    public void onRetryClick() {
        loadData(false);
    }

    @Nullable
    @Override
    public View getCardContentView() {
        return cardContent;
    }

    @Nullable
    @Override
    public View getErrorContentView() {
        return errorContainer;
    }

    @Nullable
    @Override
    public View getProgressContentView() {
        return progressBar;
    }

    @Nullable
    @Override
    public View getRetryView() {
        return retryButton;
    }

    @Nullable
    @Override
    public TextView getErrorTextView() {
        return errorTextView;
    }

    @Nullable
    @Override
    public TextView getInfoTextView() {
        return notificationTitleTextView;
    }

    @NonNull
    @Override
    public ViewState<NotificationCardView> createViewState() {
        return new ParcelableDataLceViewState<NotificationDTO, NotificationCardView>();
    }

    @NonNull
    @Override
    public ParcelableDataLceViewState<NotificationDTO, NotificationCardView> getViewState() {
        return (ParcelableDataLceViewState<NotificationDTO, NotificationCardView>) super
                .getViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        if (!isInEditMode()) {
            loadData(false);
        }
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
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        getViewState().setStateShowLoading(false);
    }

    @NonNull
    @Override
    public NotificationCardPresenter createPresenter() {
        if (isInEditMode()) return new NotificationCardPresenter();
        return dashBoardComponent.notificationCardPresenter();
    }

    @Override
    public void setData(@NonNull NotificationDTO data) {
        this.data = data;
        initUI(data);
    }

    private void initUI(@NonNull NotificationDTO data) {
        ArticleDBO article = null;
        if (data.getAllEvents().size() > 0) {
            article = data.getAllEvents().get(0);
        } else if (data.getAllLaws().size() > 0) {
            article = data.getAllLaws().get(0);
        }

        if (article != null) {
            notificationTitleTextView.setText(article.getTitle());
            icon.setImageResource(article.isViewed() ? R.drawable.ic_no_notifications : R
                    .drawable.ic_notifications);
        } else if (data.getFinesCount() > 0) {
            notificationTitleTextView.setText(R.string.new_fine);
            icon.setImageResource(R.drawable.ic_notifications);
        } else {
            notificationTitleTextView.setText(getContext().getString(R.string.we_notify_you));
            icon.setImageResource(R.drawable.ic_no_notifications);
        }
    }

    public void loadData(boolean pullToRefresh) {
        presenter.load(false);
    }

    @Override
    public void onClick(View v) {
        intentStarter.openNotificationsScreen(getContext(), data);

        analyticReporter.widgetClickEvent(Reporter.SCREEN_SERVICES, CATEGORY_NOTIFICATION,
                getInfoText());
        loadData(false);
    }

    @Override
    public void onVieweble() {
        analyticReporter.widgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_NOTIFICATION);
    }
}
