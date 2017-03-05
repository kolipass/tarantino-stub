package mobi.tarantino.stub.auto.model.database;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**

 */
@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public DatabaseHelperFactory provideDatabaseHelperFactory(Context context) {
        return new DatabaseHelperFactory(context);
    }
}
