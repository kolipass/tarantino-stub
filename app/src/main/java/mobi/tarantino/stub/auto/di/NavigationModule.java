package mobi.tarantino.stub.auto.di;


import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.model.database.DatabaseHelperFactory;

/**
 * @author Hannes Dorfmann
 */
@Module
@Singleton
public class NavigationModule {

    @NonNull
    @Singleton
    @Provides
    public IntentStarter providesIntentStarter(Context context, IPreferencesManager
            preferencesManager,
                                               AuthorizationResolver authorizationResolver,
                                               DatabaseHelperFactory databaseHelperFactory) {
        return new IntentStarter(context, preferencesManager, authorizationResolver,
                databaseHelperFactory);
    }
}
