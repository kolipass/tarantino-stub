package mobi.tarantino.stub.auto.model.database;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**

 */
@Module
public class DatabaseModule {

    @NonNull
    @Provides
    @Singleton
    public DatabaseHelperFactory provideDatabaseHelperFactory(Context context) {
        return new DatabaseHelperFactory(context);
    }
}
