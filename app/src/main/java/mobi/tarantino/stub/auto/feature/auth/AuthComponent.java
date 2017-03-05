package mobi.tarantino.stub.auto.feature.auth;

import dagger.Subcomponent;
import mobi.tarantino.stub.auto.di.AppComponent;


@AuthScope
@Subcomponent(
        modules = {AuthModule.class}
)
public interface AuthComponent extends AppComponent {

    void inject(AuthActivity test);

    void inject(InputPhoneFragment inputPhoneFragment);

    void inject(InputCodeFragment authFragment);

    AuthPresenter provideAuthPresenter();
}