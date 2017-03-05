package mobi.tarantino.stub.auto;

import rx.Scheduler;

public abstract class SchedulerProvider {
    public abstract Scheduler network();

    public abstract Scheduler ui();
}
