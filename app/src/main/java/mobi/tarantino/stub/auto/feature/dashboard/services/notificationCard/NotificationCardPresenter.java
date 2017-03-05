package mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import mobi.tarantino.stub.auto.eventbus.RefreshNotificationEvent;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;
import mobi.tarantino.stub.auto.mvp.MvpLceRxPresenter;

public class NotificationCardPresenter<T extends NotificationCardView> extends
        MvpLceRxPresenter<T, NotificationDTO> {

    protected MobiAdditionalModel model;

    public NotificationCardPresenter() {
    }

    public NotificationCardPresenter(MobiAdditionalModel model) {
        this.model = model;
    }

    public void load(boolean refresh) {
        subscribe(model.getNotificationDTO(refresh), refresh);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void attachView(T view) {
        super.attachView(view);
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshNotification(RefreshNotificationEvent notificationEvent) {
        load(false);
    }
}