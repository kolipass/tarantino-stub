package mobi.tarantino.stub.auto.feature.auth;

import dagger.Component;
import mobi.tarantino.stub.auto.di.BaseComponent;

@Component(
        dependencies = {BaseComponent.class},
        modules = {AuthModule.class}
)
@AuthScope
public interface TestAuthComponent {
}
