package mobi.tarantino.stub.auto.feature.notifications;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;

@Module
public class NotificationsModule {

    @Provides
    @NotificationsScope
    public NotificationsPresenter provideNotificationsPresenter(MobiAdditionalModel model) {
        return new NotificationsPresenter(model);
    }
}
