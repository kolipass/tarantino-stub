package mobi.tarantino.stub.auto.retrofitUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Needed for parse http responce when server return http error status
 * from http://bytes.babbel.com/en/articles/2016-03-16-retrofit2-rxjava-error-handling.html
 */

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    @NonNull
    private final RxJavaCallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJavaCallAdapterFactory.create();
    }

    @NonNull
    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @NonNull
    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {
        private final Retrofit retrofit;
        private final CallAdapter<?> wrapped;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @NonNull
        @SuppressWarnings("unchecked")
        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            return ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Func1<Throwable,
                    Observable>() {
                @NonNull
                @Override
                public Observable call(Throwable throwable) {
                    return Observable.error(asRetrofitException(throwable));
                }
            });
        }

        @Nullable
        private RetrofitException asRetrofitException(Throwable throwable) {
            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                return RetrofitException.httpError(response.raw().request().url().toString(),
                        response, retrofit);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return RetrofitException.networkError((IOException) throwable);
            }

            // We don't know what happened. We need to simply convert to an unknown error

            return RetrofitException.unexpectedError(throwable);
        }
    }
}