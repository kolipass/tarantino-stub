package mobi.tarantino.stub.auto.feature.dashboard.services.quizCard;

import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;
import mobi.tarantino.stub.auto.mvp.MvpLceRxPresenter;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**

 */
public class QuizCardPresenter extends MvpLceRxPresenter<QuizCardView, List<Question>> {

    private MobiAdditionalModel model;

    private Subscriber<Response<ResponseBody>> quizActionSubscriber;

    public QuizCardPresenter() {
    }

    public QuizCardPresenter(MobiAdditionalModel model) {
        this.model = model;
    }

    public void load() {
        subscribe(model.getQuestions(), false);
    }

    @Override
    protected void onNext(List<Question> data) {
        super.onNext(data);
    }

    public void quizLike(Question question) {
        Observable<Response<ResponseBody>> quizLikeObservable = model.likeQuestion(question);
        quizLikeObservable.subscribe(getQuizActionSubscriber());
    }

    public void quizDislike(Question question) {
        Observable<Response<ResponseBody>> quizLikeObservable = model.dislikeQuestion(question);
        quizLikeObservable.subscribe(getQuizActionSubscriber());
    }

    public void onQuizActionSuccess() {
        if (isViewAttached() && getView() != null) {
            getView().onQuizActionSuccess();
        }
    }

    public void onQuizActionError(Throwable e) {
        if (isViewAttached() && getView() != null) {
            getView().onQuizActionFailed(e);
        }
    }

    @Override
    protected void unsubscribe() {
        super.unsubscribe();
        if (quizActionSubscriber != null && !quizActionSubscriber.isUnsubscribed()) {
            quizActionSubscriber.unsubscribe();
        }
        quizActionSubscriber = null;
    }

    public Subscriber<Response<ResponseBody>> getQuizActionSubscriber() {
        quizActionSubscriber = new Subscriber<Response<ResponseBody>>() {

            @Override
            public void onCompleted() {
                QuizCardPresenter.this.onQuizActionSuccess();
            }

            @Override
            public void onError(Throwable e) {
                QuizCardPresenter.this.onQuizActionError(e);
            }

            @Override
            public void onNext(Response<ResponseBody> response) {

            }
        };
        return quizActionSubscriber;
    }

}
