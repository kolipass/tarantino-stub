package mobi.tarantino.stub.auto.model.rxHelper;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.model.ApiResponse;
import mobi.tarantino.stub.auto.model.auth.HttpApiException;
import mobi.tarantino.stub.auto.model.auth.pojo.ErrorMobiApiAnswer;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;

/**
 * При http error Retrofit кидает HttpException, но нам нужен ответ от сервера

 */

public class ParseErrorResponseFunc<N extends ApiResponse> implements Func1<Throwable,
        Observable<N>> {
    protected Gson gson;
    @Inject
    Logger logger;

    @Inject
    public ParseErrorResponseFunc<N> setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    @Inject
    public ParseErrorResponseFunc<N> setGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    private Throwable getApiError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            try {
                Response<?> response = ((HttpException) throwable)
                        .response();
                ResponseBody responseBody = response
                        .errorBody();
                if (responseBody != null) {
                    String responseBodyString = responseBody
                            .string();
                    if (!responseBodyString.isEmpty()) {
                        ErrorMobiApiAnswer answer = gson
                                .fromJson(responseBodyString, ErrorMobiApiAnswer.class);
                        if (answer != null && !answer.isEmpty()) {
                            return new HttpApiException(response, answer);
                        } else {
                            return new HttpApiException(
                                    response,
                                    new ErrorMobiApiAnswer(
                                            response.message(),
                                            String.valueOf(response.code()),
                                            responseBodyString,
                                            null
                                    )
                            );
                        }
                    }
                }

                return new HttpApiException(
                        response,
                        new ErrorMobiApiAnswer(
                                response.message(),
                                String.valueOf(response.code())
                        )
                );

            } catch (JsonSyntaxException | JsonIOException | IOException ignore) {
            }
        }
        return throwable;
    }

    @Override
    public Observable<N> call(Throwable throwable) {
        ErrorObservable<N> onSubscribe = new ErrorObservable<>();
        onSubscribe.setThrowable(getApiError(throwable));

        return Observable.create(onSubscribe);
    }
}
