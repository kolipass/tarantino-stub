package mobi.tarantino.stub.auto.di;

import android.support.annotation.NonNull;

import mobi.tarantino.stub.auto.SchedulerProvider;
import rx.Scheduler;
import rx.schedulers.Schedulers;


public class ImmediateSchedulersModule extends SchedulersModule {
    @NonNull
    @Override
    public SchedulerProvider providesSchedulers() {
        return new SchedulerProvider() {

            @Override
            public Scheduler network() {
                return Schedulers.immediate();
            }

            @Override
            public Scheduler ui() {
                return Schedulers.immediate();
            }
        };
    }
}
