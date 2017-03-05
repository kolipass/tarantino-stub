package mobi.tarantino.stub.auto.retrofitUtils.errorHandler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import mobi.tarantino.stub.auto.IntentStarter;

public class ErrorHandler {

    @NonNull
    private Map<Class, AbstractErrorHandlerStrategy> strategyMap = new HashMap<>();
    private ErrorHandlerListener listener;
    private IntentStarter intentStarter;
    private Context context;

    public ErrorHandler(Context context, IntentStarter intentStarter) {
        this.intentStarter = intentStarter;
        this.context = context;
    }

    public void setErrorHandlerListener(ErrorHandlerListener listener) {
        this.listener = listener;
    }

    @NonNull
    public ErrorHandler registerStrategy(Class clazz, @NonNull AbstractErrorHandlerStrategy
            strategy) {
        strategyMap.put(clazz, performStrategy(strategy));
        return this;
    }

    @NonNull
    private AbstractErrorHandlerStrategy performStrategy(@NonNull AbstractErrorHandlerStrategy
                                                                     strategy) {
        strategy.setIntentStarter(intentStarter);
        strategy.setContext(context);
        return strategy;
    }

    public void handleError(Throwable e) {
        AbstractErrorHandlerStrategy strategy = getStrategy(e);
        strategy.setListener(listener);
        strategy.handleError(e);
    }

    @NonNull
    private AbstractErrorHandlerStrategy getStrategy(@Nullable Throwable e) {
        AbstractErrorHandlerStrategy strategy = e == null
                ? null
                : strategyMap.get(e.getClass());
        if (strategy == null) {
            return getDefaultStrategy();
        }
        return strategy;
    }

    @NonNull
    private AbstractErrorHandlerStrategy getDefaultStrategy() {
        return new DefaultStrategy()
                .setListener(listener)
                .setIntentStarter(intentStarter);
    }

    public interface ErrorHandlerListener {
        void onHandleErrorMessage(String errorMessage);
    }
}
