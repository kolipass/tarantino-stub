package mobi.tarantino.stub.auto.retrofitUtils.errorHandler;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import mobi.tarantino.stub.auto.IntentStarter;

public class ErrorHandler {

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

    public ErrorHandler registerStrategy(Class clazz, AbstractErrorHandlerStrategy strategy) {
        strategyMap.put(clazz, performStrategy(strategy));
        return this;
    }

    private AbstractErrorHandlerStrategy performStrategy(AbstractErrorHandlerStrategy strategy) {
        strategy.setIntentStarter(intentStarter);
        strategy.setContext(context);
        return strategy;
    }

    public void handleError(Throwable e) {
        AbstractErrorHandlerStrategy strategy = getStrategy(e);
        strategy.setListener(listener);
        strategy.handleError(e);
    }

    private AbstractErrorHandlerStrategy getStrategy(Throwable e) {
        AbstractErrorHandlerStrategy strategy = e == null
                ? null
                : strategyMap.get(e.getClass());
        if (strategy == null) {
            return getDefaultStrategy();
        }
        return strategy;
    }

    private AbstractErrorHandlerStrategy getDefaultStrategy() {
        return new DefaultStrategy()
                .setListener(listener)
                .setIntentStarter(intentStarter);
    }

    public interface ErrorHandlerListener {
        void onHandleErrorMessage(String errorMessage);
    }
}
