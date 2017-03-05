package mobi.tarantino.stub.auto.retrofitUtils.errorHandler;

import mobi.tarantino.stub.auto.R;

public class UnknownHostExceptionStrategy extends AbstractErrorHandlerStrategy {
    @Override
    boolean isCritical(Throwable e) {
        return false;
    }

    @Override
    void processError(Throwable e) {
        listener.onHandleErrorMessage(context.getString(R.string.unavaliable_internet_error));
    }

    @Override
    void doOnCritical(Throwable e) {

    }
}
