package mobi.tarantino.stub.auto.retrofitUtils.errorHandler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import mobi.tarantino.stub.auto.IntentStarter;

public abstract class AbstractErrorHandlerStrategy {

    protected IntentStarter intentStarter;
    protected ErrorHandler.ErrorHandlerListener listener;
    protected Context context;

    protected static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    @NonNull
    public AbstractErrorHandlerStrategy setIntentStarter(IntentStarter intentStarter) {
        this.intentStarter = intentStarter;
        return this;
    }

    @NonNull
    public AbstractErrorHandlerStrategy setListener(ErrorHandler.ErrorHandlerListener listener) {
        this.listener = listener;
        return this;
    }

    @NonNull
    public AbstractErrorHandlerStrategy setContext(Context context) {
        this.context = context;
        return this;
    }

    public void handleError(Throwable e) {
        if (listener != null) {
            if (isCritical(e)) {
                doOnCritical(e);
            } else {
                processError(e);
            }
        }
    }

    abstract boolean isCritical(Throwable e);

    abstract void processError(Throwable e);

    abstract void doOnCritical(Throwable e);
}
