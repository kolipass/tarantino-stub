package mobi.tarantino.stub.auto.analytics;

import com.google.android.gms.tagmanager.DataLayer;

import static mobi.tarantino.stub.auto.Consts.Analytics.OPEN_SCREEN;
import static mobi.tarantino.stub.auto.Consts.Analytics.SIMPLE_EVENT;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_EVENT_ACTION;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_EVENT_CATEGORY;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_EVENT_LABEL;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_GA_CLIENT_ID;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_SCREEN_NAME;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_USER_ID;

public class MobiGoogleAnalytic extends AbstractAnalytic {

    private DataLayer dataLayer;

    public MobiGoogleAnalytic(DataLayer dataLayer) {
        this.dataLayer = dataLayer;
    }


    @Override
    protected void pushSimpleEvent(String gaClientId, String userId, String screenName, String
            eventCategory, String eventAction, String eventLabel) {
        dataLayer.pushEvent(SIMPLE_EVENT, DataLayer.mapOf(
                TAG_GA_CLIENT_ID, gaClientId,
                TAG_USER_ID, userId,
                TAG_SCREEN_NAME, screenName,
                TAG_EVENT_CATEGORY, eventCategory,
                TAG_EVENT_ACTION, eventAction,
                TAG_EVENT_LABEL, eventLabel));
    }

    @Override
    protected void pushOpenScreen(String gaClientId, String userId, String screenName) {
        dataLayer.pushEvent(OPEN_SCREEN, DataLayer.mapOf(
                TAG_GA_CLIENT_ID, gaClientId,
                TAG_USER_ID, userId,
                TAG_SCREEN_NAME, screenName));
    }
}
