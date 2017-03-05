package mobi.tarantino.stub.auto.feature.notifications;

import android.support.annotation.NonNull;

import mobi.tarantino.stub.auto.eventbus.RefreshNotificationEvent;
import mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard
        .NotificationCardPresenter;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;


public class NotificationsPresenter extends NotificationCardPresenter<NotificationsView> {

    public NotificationsPresenter(MobiAdditionalModel model) {
        super(model);
    }

    @Override
    public void onRefreshNotification(@NonNull RefreshNotificationEvent notificationEvent) {

        if (notificationEvent.getType() != RefreshNotificationEvent.Type.UPDATE) {
            super.onRefreshNotification(notificationEvent);
        }
    }

}
