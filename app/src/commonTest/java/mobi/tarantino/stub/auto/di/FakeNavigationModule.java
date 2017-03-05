package mobi.tarantino.stub.auto.di;


import mobi.tarantino.stub.auto.IntentStarter;

import static org.mockito.Mockito.mock;

/**
 * @author Hannes Dorfmann
 */
public class FakeNavigationModule extends NavigationModule {

    public IntentStarter providesIntentStarter() {
        return mock(IntentStarter.class);
    }
}
