package mobi.tarantino.stub.auto.analytics;

public class MobiFabricAnalytic extends AbstractAnalytic {

    @Override
    protected void pushSimpleEvent(String gaClientId, String userId, String screenName, String
            eventCategory, String eventAction, String eventLabel) {

    }

    @Override
    protected void pushOpenScreen(String gaClientId, String userId, String screenName) {

    }
}
