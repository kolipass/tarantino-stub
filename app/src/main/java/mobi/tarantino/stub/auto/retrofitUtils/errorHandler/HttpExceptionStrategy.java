package mobi.tarantino.stub.auto.retrofitUtils.errorHandler;

import retrofit2.adapter.rxjava.HttpException;

public class HttpExceptionStrategy extends AbstractErrorHandlerStrategy {
    @Override
    boolean isCritical(Throwable e) {
        HttpException exception = (HttpException) e;
        switch (exception.code()) {
            case 401:
                return true;
        }
        return false;
    }

    @Override
    void processError(Throwable e) {
        HttpException exception = (HttpException) e;
        listener.onHandleErrorMessage(exception.getLocalizedMessage());
    }

    @Override
    void doOnCritical(Throwable e) {
        intentStarter.logout();
    }
}
