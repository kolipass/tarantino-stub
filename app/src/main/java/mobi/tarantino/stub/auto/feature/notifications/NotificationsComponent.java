package mobi.tarantino.stub.auto.feature.notifications;

import dagger.Subcomponent;
import mobi.tarantino.stub.auto.di.AppComponent;

@Subcomponent(modules = NotificationsModule.class)
@NotificationsScope
public interface NotificationsComponent extends AppComponent {

    void inject(NotificationsActivity notificationsActivity);

    NotificationsPresenter notificationsPresenter();
}
