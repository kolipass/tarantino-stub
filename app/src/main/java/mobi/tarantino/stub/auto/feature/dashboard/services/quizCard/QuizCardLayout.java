package mobi.tarantino.stub.auto.feature.dashboard.services.quizCard;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

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
import mobi.tarantino.stub.auto.helper.ViewHelper;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;

import static mobi.tarantino.stub.auto.analytics.Reporter.CATEGORY_QUIZ;

/**

 */
public class QuizCardLayout extends AbstractCardLayout<List<Question>, QuizCardView,
        QuizCardPresenter>
        implements QuizCardView, ViewSwitcher.ViewFactory, OnBeUserVieweble {

    @BindView(R.id.quiz_textView)
    TextSwitcher quizTextView;
    @BindView(R.id.like_button)
    View likeButton;
    @BindView(R.id.dislike_button)
    View dislikeButton;
    @BindView(R.id.progress_quiz_card)
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
    @BindView(R.id.quiz_controls)
    ViewGroup quizControls;

    private List<Question> data = new ArrayList<>();
    private DashBoardComponent dashBoardComponent;
    private boolean analyticSend;

    public QuizCardLayout(Context context) {
        super(context);
    }

    public QuizCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuizCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QuizCardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        initQuizTextAnimation();
    }

    private void initQuizTextAnimation() {
        quizTextView.setInAnimation(getContext(), android.R.anim.slide_in_left);
        quizTextView.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        quizTextView.setFactory(this);
    }

    @Override
    public String getErrorText() {
        return null;
    }

    @Override
    public String getInfoText() {
        return getContext().getString(R.string.your_opinion);
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

    @NonNull
    @Override
    public ViewState<QuizCardView> createViewState() {
        return new CastedArrayListLceViewState<List<Question>, QuizCardView>();
    }

    @Override
    public void onNewViewStateInstance() {
        if (!isInEditMode()) {
            loadData(false);
        }
    }

    @NonNull
    @Override
    public QuizCardPresenter createPresenter() {
        if (isInEditMode()) return new QuizCardPresenter();
        dashBoardComponent = MobiApplication
                .get(getContext())
                .getComponentContainer()
                .getDashBoardComponent((DashBoardActivity) getContext());
        return dashBoardComponent.quizCardPresenter();
    }

    @Override
    public void showContent() {
        super.showContent();
    }


    protected boolean hasNextQuiz() {
        return data.size() > 0;
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
    }

    @SuppressWarnings("unchecked cast")
    @Override
    public CastedArrayListLceViewState<List<Question>, QuizCardView> getViewState() {
        return (CastedArrayListLceViewState<List<Question>, QuizCardView>) super.getViewState();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
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
    public void setData(List<Question> data) {
        this.data.clear();

        if (data != null && data.size() > 0) {
            this.data.addAll(data);
            initQuiz();
        } else {
            setVisibility(GONE);
        }
    }

    protected void initQuiz() {
        if (hasNextQuiz()) {
            quizTextView.setText(data.get(0).getContent());

            if (!analyticSend) {
                analyticReporter.widgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_QUIZ);
                analyticSend = true;
            }
        } else {
            setVisibility(GONE);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (!isInEditMode()) {
            presenter.load();
        }
    }

    @Override
    public void onQuizActionSuccess() {
        enableDisableControll(true);
        initNextQuiz();
    }

    @Override
    public void onQuizActionFailed(Throwable e) {
        enableDisableControll(true);
        initNextQuiz();
    }

    private void initNextQuiz() {
        if (data != null && data.size() > 0) {
            data.remove(0);
        }
        initQuiz();
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(getContext(), R.style.text_small);
        } else {
            textView.setTextAppearance(R.style.text_small);
        }
        return textView;
    }

    @OnClick(R.id.like_button)
    public void onLikeButtonClick() {
        enableDisableControll(false);
        if (data != null && data.size() > 0) {
            presenter.quizLike(data.get(0));
        }
        analyticReporter.widgetClickEvent(Reporter.SCREEN_SERVICES, CATEGORY_QUIZ, "like");
    }

    @OnClick(R.id.dislike_button)
    public void onDislikeButtonClick() {
        enableDisableControll(false);
        if (data != null && data.size() > 0) {
            presenter.quizDislike(data.get(0));
        }
        analyticReporter.widgetClickEvent(Reporter.SCREEN_SERVICES, CATEGORY_QUIZ, "dislike");
    }

    @OnClick(R.id.update_button)
    public void onUpdateButtonClick() {
        loadData(true);
        analyticReporter.refreshWidgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_QUIZ);
    }

    private void enableDisableControll(boolean enabled) {
        ViewHelper.enableDisableViewGroup(quizControls, enabled);
    }

    @Override
    public void onVieweble() {
        if (hasNextQuiz()) {
            analyticReporter.widgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_QUIZ);
        }
    }
}
