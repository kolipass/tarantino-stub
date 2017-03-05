package mobi.tarantino.stub.auto.di;

import javax.inject.Singleton;

import dagger.Component;
import mobi.tarantino.stub.auto.model.database.DatabaseModule;


@Singleton
@Component(modules = {
        BaseModule.class,
        SchedulersModule.class,
        AuthApiModule.class,
        NavigationModule.class,
        AdditionalDataModule.class,
        DatabaseModule.class
})
public interface BaseTestComponent extends BaseComponent {
}
