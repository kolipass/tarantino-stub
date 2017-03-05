package mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy;

import android.content.Context;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.eventbus.RefreshNotificationEvent;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;

public abstract class AbstractGcmHandlerStrategy {

    protected Context context;
    @Inject
    AnalyticReporter analyticReporter;
    @Inject
    IPreferencesManager preferencesManager;
    @Inject
    MobiAdditionalModel model;
    @Inject
    Logger logger;

    public AbstractGcmHandlerStrategy(Context context) {
        this.context = context;
        MobiApplication.get(context).getComponentContainer().getAnalyticComponent().inject(this);
    }

    public void handleGcmMessage(Bundle bundle) {
        logger.d(bundle);
        processData(bundle);
        createNotification(bundle);
        EventBus.getDefault().post(new RefreshNotificationEvent(RefreshNotificationEvent.Type.ADD));
    }

    protected abstract void createNotification(Bundle bundle);

    protected abstract void processData(Bundle bundle);
}
