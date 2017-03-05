package mobi.tarantino.stub.auto.di;

import mobi.tarantino.stub.auto.SchedulerProvider;
import rx.Scheduler;
import rx.schedulers.Schedulers;


public class ImmediateSchedulersModule extends SchedulersModule {
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
