package mobi.tarantino.stub.auto.feature.dashboard.services.quizCard;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;

/**

 */
public interface QuizCardView extends MvpLceView<List<Question>> {
    void onQuizActionSuccess();

    void onQuizActionFailed(Throwable e);
}
