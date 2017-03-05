package mobi.tarantino.stub.auto.retrofitUtils.errorHandler;

public class DefaultStrategy extends AbstractErrorHandlerStrategy {

    @Override
    protected boolean isCritical(Throwable e) {
        return false;
    }

    @Override
    protected void processError(Throwable e) {
        listener.onHandleErrorMessage(e.getLocalizedMessage());
    }

    @Override
    protected void doOnCritical(Throwable e) {

    }
}
