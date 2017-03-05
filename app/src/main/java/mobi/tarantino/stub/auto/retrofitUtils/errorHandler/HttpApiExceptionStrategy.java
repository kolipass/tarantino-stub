package mobi.tarantino.stub.auto.retrofitUtils.errorHandler;

import android.text.TextUtils;

import java.util.Locale;

import mobi.tarantino.stub.auto.model.auth.HttpApiException;
import mobi.tarantino.stub.auto.model.auth.pojo.ErrorMobiApiAnswer;

public class HttpApiExceptionStrategy extends AbstractErrorHandlerStrategy {
    @Override
    protected boolean isCritical(Throwable e) {
        int code = getErrorCode(e);
        return code == 401 || code == 772 || code == 862 || code == 892;
    }

    protected int getErrorCode(Throwable e) {
        if (e instanceof HttpApiException) {
            HttpApiException exception = (HttpApiException) e;
            return exception.code();
        }
        return -1;
    }

    @Override
    void processError(Throwable e) {
        listener.onHandleErrorMessage(getErrorMessage((HttpApiException) e));
    }

    @Override
    void doOnCritical(Throwable e) {
        HttpApiException exception = (HttpApiException) e;
        int code = exception.code();
        switch (code) {
            case 401:
                unauthorizedException(exception);
                break;
            case 772:
                appVersionException(exception);
                break;
            case 862:
                tokenException(exception);
                break;
            case 892:
                blockTokenException(exception);
                break;
        }
    }

    private void blockTokenException(HttpApiException exception) {
        if (listener != null) listener.onHandleErrorMessage(getErrorMessage(exception));
        intentStarter.logout();
    }

    private void tokenException(HttpApiException exception) {
        if (listener != null) listener.onHandleErrorMessage(getErrorMessage(exception));
        intentStarter.logout();
    }

    private void appVersionException(HttpApiException exception) {
        if (listener != null) listener.onHandleErrorMessage(getErrorMessage(exception));
        intentStarter.logout();
        intentStarter.updateApp();
    }

    protected void unauthorizedException(HttpApiException exception) {
        if (listener != null) listener.onHandleErrorMessage(getErrorMessage(exception));
        intentStarter.logout();
    }

    private String getErrorMessage(HttpApiException e) {
        String message = "";
        ErrorMobiApiAnswer apiAnswer = e.getApiAnswer();
        if (apiAnswer != null) {
            if (!TextUtils.isEmpty(apiAnswer.getErrorCode())) {
                message = append(message, apiAnswer.getErrorCode());
            }

            if (!message.isEmpty()) message = append(message, ": ");

            if (!TextUtils.isEmpty(getUserMessage(apiAnswer))) {
                message = append(message, getUserMessage(apiAnswer));
            } else {
                message = append(message, apiAnswer.getError());
                message = append(message, "\n");
                message = append(message, apiAnswer.getErrorDescription());
            }
        } else {
            message = append(message, String.valueOf(e.code()));
            message = append(message, e.message());
        }

        return message;
    }

    private String getUserMessage(ErrorMobiApiAnswer apiAnswer) {
        String userMessage = apiAnswer.getUserMessage();
        if (!Locale.getDefault().getLanguage().equals("ru")
                || TextUtils.isEmpty(userMessage)) {

            String errorDescription = apiAnswer.getErrorDescription();
            if (!TextUtils.isEmpty(errorDescription)) {
                userMessage = errorDescription;
            }
        }
        return userMessage;
    }

    private String append(String base, String suffix) {
        if (isEmpty(suffix)) {
            return base;
        } else {
            if (!isEmpty(base)) {
                base += " ";
            }
            base += suffix;
            return base;
        }
    }
}
