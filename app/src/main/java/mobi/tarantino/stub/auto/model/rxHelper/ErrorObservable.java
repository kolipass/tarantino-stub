package mobi.tarantino.stub.auto.model.rxHelper;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

public class ErrorObservable<N> implements Observable.OnSubscribe<N> {
    private Throwable throwable;

    @Override
    public void call(@NonNull Subscriber<? super N> subscriber) {
        subscriber.onError(throwable);
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @NonNull
    public ErrorObservable setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }
}
