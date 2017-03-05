package mobi.tarantino.stub.auto.di;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.SchedulerProvider;
import mobi.tarantino.stub.auto.feature.auth.AuthComponent;
import mobi.tarantino.stub.auto.feature.auth.AuthModule;
import mobi.tarantino.stub.auto.feature.demoTour.DemoTourActivity;
import mobi.tarantino.stub.auto.feature.notifications.NotificationsActivity;
import mobi.tarantino.stub.auto.feature.notifications.NotificationsComponent;
import mobi.tarantino.stub.auto.feature.notifications.NotificationsModule;
import mobi.tarantino.stub.auto.gcm.MobiGcmListenerService;
import mobi.tarantino.stub.auto.gcm.RegistrationIntentService;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalDataApi;
import mobi.tarantino.stub.auto.model.auth.MobiAuthApi;
import mobi.tarantino.stub.auto.model.database.DatabaseHelperFactory;
import mobi.tarantino.stub.auto.model.database.DatabaseModule;
import mobi.tarantino.stub.auto.model.location.LocationModel;
import mobi.tarantino.stub.auto.retrofitUtils.HttpLoggingInterceptor;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;


@Singleton
@Component(modules = {
        BaseModule.class,
        SchedulersModule.class,
        AuthApiModule.class,
        NavigationModule.class,
        AdditionalDataModule.class,
        DatabaseModule.class,
})
public interface BaseComponent extends AppComponent {
    SessionComponent.Builder buildSessionComponent();

    AuthComponent plus(AuthModule trafficFinesModule);

    NotificationsComponent plus(NotificationsModule notificationsModule);

    Context context();

    Logger logger();

    Picasso providesPicasso();

    HttpLoggingInterceptor.Logger httpLoggingInterceptor();

    MobiAuthApi mobiAuthApi();

    MobiAdditionalDataApi mobiAdditionalDataApi();

    Gson gson();

    IPreferencesManager providesPreferencesManager();

    SchedulerProvider providesSchedulers();

    DatabaseHelperFactory providesDatabaseHelperFactory();

    LocationModel provideLocationModel();

    ErrorHandler errorHandler();

    IntentStarter providesIntentStarter();

    void inject(MobiApplication mobiApplication);

    void inject(DependencyComponentManager dependencyComponentManager);

    void inject(DemoTourActivity demoTourActivity);

    void inject(IntentStarter intentStarter);

    void inject(RegistrationIntentService registrationIntentService);

    void inject(MobiGcmListenerService mobiGcmListenerService);

    void inject(NotificationsActivity notificationsActivity);
}
