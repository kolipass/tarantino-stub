package mobi.tarantino.stub.auto.feature.auth;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.model.auth.AuthModel;
import rx.Subscriber;
import rx.Subscription;

/**

 */

public class SmsSendTimerPresenter extends MvpBasePresenter<AuthView> {

    private AuthModel model;
    private Subscription timerSubscription;
    private int limitSec = 30;

    @Inject
    public SmsSendTimerPresenter(AuthModel model) {
        this.model = model;
    }

    public void setLimit(int limitSec) {
        this.limitSec = limitSec;
    }

    public void init() {
        if (timerSubscription != null && !timerSubscription.isUnsubscribed())
            timerSubscription.unsubscribe();
        timerStart();
        timerSubscription = model.getSmsTimer(limitSec).subscribe(getTimerSubscriber());
    }

    private Subscriber<? super Long> getTimerSubscriber() {
        return new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                timerCompete();
            }

            @Override
            public void onError(Throwable e) {
                timerError(e);
            }

            @Override
            public void onNext(Long seconds) {
                timerNext(seconds);
            }
        };
    }

    private void timerNext(Long seconds) {
        if (getView() != null && isViewAttached()) {
            getView().onTimerNext(limitSec - seconds - 1);
        }
    }

    private void timerError(Throwable e) {
        if (getView() != null && isViewAttached()) {
            getView().onTimerError();
        }
    }

    private void timerCompete() {
        if (getView() != null && isViewAttached()) {
            getView().onTimerComplete();
        }
    }

    private void timerStart() {
        if (getView() != null && isViewAttached()) {
            getView().onTimerStart();
        }
    }
}
