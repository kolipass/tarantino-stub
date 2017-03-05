package mobi.tarantino.stub.auto.feature.dashboard.services.quizCard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.mock.MockContext;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.ArrayList;
import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**

 */
public class QuizCardPresenterTest {

    QuizCardLayout view;
    MobiAdditionalModel model;
    QuizCardPresenter presenter;
    Context context;

    @Before
    public void setUp() {
        context = mock(MockContext.class);
        when(context.getString(anyInt())).thenReturn("");
        view = spy(new QuizCardLayout(context) {
            @Override
            protected void initQuiz() {

            }
        });

        when(view.getContext()).thenReturn(context);
        when(view.createViewState()).thenReturn(new CastedArrayListLceViewState<List<Question>,
                QuizCardView>());

        model = mock(MobiAdditionalModel.class);
        presenter = new QuizCardPresenter(model);
        presenter.attachView(view);
    }

    @Test
    public void modelReturnEmptyListTest() {
        Observable<List<Question>> modelResult = Observable.just(getQuestionListWithSize(0));
        when(model.getQuestions()).thenReturn(modelResult);
        presenter.load();
        verify(view).showContent();
        verify(view).setVisibility(View.GONE);
    }

    @Test
    public void modelReturnNotEmptyListTest() {
        Observable<List<Question>> modelResult = Observable.just(getQuestionListWithSize(3));
        when(model.getQuestions()).thenReturn(modelResult);
        presenter.load();
        verify(view).showContent();
        verify(view, VerificationModeFactory.atMost(0)).setVisibility(View.GONE);
    }

    @Test
    public void presenterLikeTest() {
        List<Question> questionListWithSize = getQuestionListWithSize(3);
        Observable<List<Question>> modelResult = Observable.just(questionListWithSize);
        when(model.getQuestions()).thenReturn(modelResult);
        presenter.load();
        Response<ResponseBody> response = null;
        when(model.likeQuestion(questionListWithSize.get(0))).thenReturn(Observable.just(response));
        presenter.quizLike(questionListWithSize.get(0));
        verify(view).onQuizActionSuccess();
    }

    @Test
    public void presenterDisLikeTest() {
        List<Question> questionListWithSize = getQuestionListWithSize(3);
        Observable<List<Question>> modelResult = Observable.just(questionListWithSize);
        when(model.getQuestions()).thenReturn(modelResult);
        presenter.load();
        Response<ResponseBody> response = null;
        when(model.dislikeQuestion(questionListWithSize.get(0))).thenReturn(Observable.just
                (response));
        presenter.quizDislike(questionListWithSize.get(0));
        verify(view).onQuizActionSuccess();
    }

    @NonNull
    public List<Question> getQuestionListWithSize(int itemCount) {
        List<Question> questionList = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            questionList.add(new Question());
        }
        return questionList;
    }

}