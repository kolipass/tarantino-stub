package mobi.tarantino.stub.auto.feature.notifications;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;

@Module
public class NotificationsModule {

    @NonNull
    @Provides
    @NotificationsScope
    public NotificationsPresenter provideNotificationsPresenter(MobiAdditionalModel model) {
        return new NotificationsPresenter(model);
    }
}
