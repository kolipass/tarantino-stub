package mobi.tarantino.stub.auto.di;

import android.content.Context;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.PreferencesManager;

import static org.mockito.Mockito.mock;

public class BaseTestModule extends BaseModule {

    public BaseTestModule(Context context) {
        super(context);
    }

    @Override
    public Logger providesLogger() {
        return new Logger() {
            @Override
            public void d(String message) {
                Logger.DEFAULT.log(message);
            }
        };
    }

    public IPreferencesManager providesPreferencesManager() {
        return mock(PreferencesManager.class);
    }


}
