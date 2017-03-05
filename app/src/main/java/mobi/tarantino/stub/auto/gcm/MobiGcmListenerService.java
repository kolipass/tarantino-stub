package mobi.tarantino.stub.auto.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.HashMap;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy.AbstractGcmHandlerStrategy;
import mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy.EventGcmHandler;
import mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy.FineGcmHandler;
import mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy.LawGcmHandler;

import static mobi.tarantino.stub.auto.Consts.Notification.CATEGORY_NEW_EVENT;
import static mobi.tarantino.stub.auto.Consts.Notification.CATEGORY_NEW_FINE;
import static mobi.tarantino.stub.auto.Consts.Notification.CATEGORY_NEW_LAW;
import static mobi.tarantino.stub.auto.Consts.Notification.KEY_CATEGORY;

/**

 */

public class MobiGcmListenerService extends GcmListenerService {

    @Inject
    IPreferencesManager preferencesManager;

    private HashMap<String, AbstractGcmHandlerStrategy> handleStrategyMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        MobiApplication.get(this).getComponentContainer().getBaseComponent().inject(this);
        initHandleStrategy();
    }

    private void initHandleStrategy() {
        handleStrategyMap.put(CATEGORY_NEW_FINE, new FineGcmHandler(this));
        handleStrategyMap.put(CATEGORY_NEW_LAW, new LawGcmHandler(this));
        handleStrategyMap.put(CATEGORY_NEW_EVENT, new EventGcmHandler(this));
    }

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        String category = bundle.getString(KEY_CATEGORY);
        if (category != null) {
            try {
                AbstractGcmHandlerStrategy handleStrategy = getHandleStrategy(category);
                handleStrategy.handleGcmMessage(bundle);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private AbstractGcmHandlerStrategy getHandleStrategy(String category) throws Throwable {
        AbstractGcmHandlerStrategy strategy = handleStrategyMap.get(category);
        if (strategy == null)
            throw new Throwable("No strategy associated with " + category + " category");
        return strategy;
    }
}
