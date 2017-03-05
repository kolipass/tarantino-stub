package mobi.tarantino.stub.auto.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.SchedulerProvider;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


@Module
public class SchedulersModule {
    @Provides
    @Singleton
    SchedulerProvider providesSchedulers() {
        return new SchedulerProvider() {

            @Override
            public Scheduler network() {
                return Schedulers.io();
            }

            @Override
            public Scheduler ui() {
                return AndroidSchedulers.mainThread();
            }
        };
    }
}
