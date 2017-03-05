package mobi.tarantino.stub.auto.di;

import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.analytics.AbstractAnalytic;

import static mobi.tarantino.stub.auto.Consts.Analytics.OPEN_SCREEN;
import static mobi.tarantino.stub.auto.Consts.Analytics.SIMPLE_EVENT;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_EVENT_ACTION;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_EVENT_CATEGORY;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_EVENT_LABEL;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_GA_CLIENT_ID;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_SCREEN_NAME;
import static mobi.tarantino.stub.auto.Consts.Analytics.TAG_USER_ID;


public class LocalAnalytic extends AbstractAnalytic {

    private Logger logger;

    public LocalAnalytic(Logger logger) {
        super();
        this.logger = logger;
    }


    @Override
    protected void pushSimpleEvent(String gaClientId, String userId,

                                   String screenName, String eventCategory,
                                   String eventAction, String eventLabel) {
        String logMessage = SIMPLE_EVENT
                + TAG_GA_CLIENT_ID + ": " + gaClientId
                + TAG_USER_ID + ": " + userId
                + TAG_SCREEN_NAME + ": " + screenName
                + TAG_EVENT_CATEGORY + ": " + eventCategory
                + TAG_EVENT_ACTION + ": " + eventAction
                + TAG_EVENT_LABEL + ": " + eventLabel;
        logger.d(logMessage);
    }

    @Override
    protected void pushOpenScreen(String gaClientId, String userId,
                                  String screenName) {
        String logMessage = OPEN_SCREEN + ": "
                + TAG_GA_CLIENT_ID + ": " + gaClientId
                + TAG_USER_ID + ": " + userId
                + TAG_SCREEN_NAME + ": " + screenName;
        logger.d(logMessage);
    }
}
