package mobi.tarantino.stub.auto.feature.splashScreen;

import dagger.Component;
import mobi.tarantino.stub.auto.di.BaseComponent;
import mobi.tarantino.stub.auto.di.PerActivity;

@PerActivity
@Component(dependencies = {BaseComponent.class}
)
public interface SplashComponent {
    void inject(SplashActivity splashActivity);
}
