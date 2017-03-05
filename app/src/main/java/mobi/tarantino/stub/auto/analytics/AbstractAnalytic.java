package mobi.tarantino.stub.auto.analytics;


public abstract class AbstractAnalytic {

    protected abstract void pushSimpleEvent(String gaClientId, String userId, String screenName,
                                            String eventCategory, String eventAction, String
                                                    eventLabel);

    protected abstract void pushOpenScreen(String gaClientId, String userId, String screenName);
}
