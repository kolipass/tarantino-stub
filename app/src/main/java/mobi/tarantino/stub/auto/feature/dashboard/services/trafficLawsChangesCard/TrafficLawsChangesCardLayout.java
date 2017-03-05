package mobi.tarantino.stub.auto.feature.dashboard.services.trafficLawsChangesCard;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.ParcelableDataLceViewState;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.feature.dashboard.AbstractCardLayout;
import mobi.tarantino.stub.auto.feature.dashboard.ArticleListener;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.feature.dashboard.services.OnBeUserVieweble;
import mobi.tarantino.stub.auto.helper.AnimationHelper;
import mobi.tarantino.stub.auto.model.additionalData.AdditionalApiHelper;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

import static mobi.tarantino.stub.auto.analytics.Reporter.CATEGORY_TRAFFICLAWS;

public class TrafficLawsChangesCardLayout extends AbstractCardLayout<Article,
        TrafficLawsChangesCardView, TrafficLawsChangesCardPresenter>
        implements TrafficLawsChangesCardView, OnBeUserVieweble {


    @BindView(R.id.changes_traffic_laws_title_textView)
    TextView title;


    @BindView(R.id.changes_traffic_laws_image)
    ImageView image;


    @BindView(R.id.progress_changes_in_traffic_laws)
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


    @BindView(R.id.update_button)
    ImageView updateButton;


    @BindView(R.id.changes_trafic_laws_read_more_button)
    View readAllButton;

    @BindView(R.id.changes_traffic_laws_read_button)
    View readArticleButton;

    @Inject
    Picasso picasso;

    @Inject
    TrafficLawsChangesCardPresenter presenter;

    @Inject
    ArticleListener articleListener;
    @Nullable
    private Article data;

    public TrafficLawsChangesCardLayout(Context context) {
        super(context);
    }

    public TrafficLawsChangesCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrafficLawsChangesCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TrafficLawsChangesCardLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            presenter = new TrafficLawsChangesCardPresenter();
        } else {
            ButterKnife.bind(this);
            MobiApplication
                    .get(getContext())
                    .getComponentContainer()
                    .getDashBoardComponent((DashBoardActivity) getContext())
                    .inject(this);
        }
    }

    @NonNull
    @Override
    public ParcelableDataLceViewState<Article, TrafficLawsChangesCardView> getViewState() {
        return (ParcelableDataLceViewState<Article, TrafficLawsChangesCardView>) super
                .getViewState();
    }

    @NonNull
    @Override
    public ParcelableDataLceViewState<Article, TrafficLawsChangesCardView> createViewState() {
        return new ParcelableDataLceViewState<>();
    }

    @Override
    public void onNewViewStateInstance() {
        loadData(true);
    }

    @NonNull
    @Override
    public TrafficLawsChangesCardPresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.changes_traffic_laws_read_button)
    public void onReadCurrentClick(View view) {
        if (articleListener != null) {
            articleListener.showArticle(ArticleDBO.TYPE_LAW, data);
        }
        analyticReporter.widgetClickEvent(Reporter.SCREEN_SERVICES, CATEGORY_TRAFFICLAWS,
                "readArticle");
    }

    @OnClick(R.id.changes_trafic_laws_read_more_button)
    public void onReadMoreClick(View view) {
        if (articleListener != null) {
            articleListener.showArticles();
        }
        analyticReporter.widgetClickEvent(Reporter.SCREEN_SERVICES, CATEGORY_TRAFFICLAWS,
                "readMore");
    }

    @OnClick(R.id.retry_button)
    public void onRetryButton(View view) {
        analyticReporter.refreshWidgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_TRAFFICLAWS);
        loadData(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        getViewState().setStateShowLoading(false);
    }

    @NonNull
    @Override
    protected String getLoadingText() {
        return getContext().getString(R.string.data_loading);
    }

    @Nullable
    @Override
    protected TextView getLoadingTextView() {
        return infoTextView;
    }

    @Nullable
    @Override
    public String getErrorText() {
        return null;
    }

    @NonNull
    @Override
    public String getInfoText() {
        return getContext().getString(R.string.changes_in_traffic_laws);
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
    public void setData(@Nullable Article data) {
        this.data = data;
        if (!isInEditMode()) {
            if (data != null) {
                picasso.load(AdditionalApiHelper.getImagePath(data))
                        .placeholder(R.drawable.pdd_placeholder)
                        .into(image);
                title.setText(data.getTitle());
                readArticleButton.setVisibility(VISIBLE);
            } else {
                image.setImageResource(R.drawable.ic_like);
                title.setText(R.string.no_new_laws);
                readArticleButton.setVisibility(GONE);
            }
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

    @OnClick(R.id.update_button)
    public void onUpdateButtonClick() {
        loadData(true);
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
        analyticReporter.widgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_TRAFFICLAWS);
    }

}
