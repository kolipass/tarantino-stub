package mobi.tarantino.stub.auto.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**

 */

public class MvpLceMultiRxPresenter<V extends MvpView> extends MvpBasePresenter<V> {

    private SubscriptionList subscriptionList;

    public MvpLceMultiRxPresenter() {
        subscriptionList = new SubscriptionList();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            unsubscribe();
        }
    }

    protected void unsubscribe() {
        subscriptionList.unsubscribe();
        subscriptionList = new SubscriptionList();
    }

    public void addSubscription(Subscription subscription) {
        subscriptionList.add(subscription);
    }


}
