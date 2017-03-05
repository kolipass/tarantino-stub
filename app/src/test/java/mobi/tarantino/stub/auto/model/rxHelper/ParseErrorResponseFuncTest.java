package mobi.tarantino.stub.auto.model.rxHelper;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.di.BaseComponent;
import mobi.tarantino.stub.auto.di.Util;
import mobi.tarantino.stub.auto.model.ApiResponse;
import mobi.tarantino.stub.auto.model.auth.HttpApiException;
import mobi.tarantino.stub.auto.model.auth.pojo.ErrorMobiApiAnswer;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResponseBody.class)
public class ParseErrorResponseFuncTest {
    public static final OnErrorAction errorAction = new OnErrorAction();
    Gson gson;
    Logger logger;

    @Before
    public void setUp() {
        BaseComponent baseComponent = Util.getFakeComponent();
        gson = baseComponent.gson();
        logger = baseComponent.logger();

    }

    @Test
    public void testNonApiErrorThrow() {
        TestSubscriber<ApiResponse> subscriber = new TestSubscriber<>();

        RuntimeException throwable = new RuntimeException();
        new ParseErrorResponseFunc<>()
                .setGson(new Gson()).call(throwable)
                .subscribe(subscriber);
        subscriber.assertError(throwable);
    }

    @Test
    public void testApiError() {
        TestSubscriber<ApiResponse> subscriber = new TestSubscriber<>();
        Response<ApiResponse> response = getApiResponseResponse(true);

        new ParseErrorResponseFunc<>()
                .setGson(gson)
                .call(new HttpException(response))
                .doOnError(errorAction)
                .subscribe(subscriber);


        assertThat(errorAction.error, instanceOf(HttpApiException.class));

        subscriber.assertTerminalEvent();
        subscriber.assertNotCompleted();
    }

    @NonNull
    private Response<ApiResponse> getApiResponseResponse(boolean validJson) {
        final String serverResponse = validJson ?
                gson.toJson(new ErrorMobiApiAnswer(
                        "simpleError",
                        "simpleCode",
                        "simpleDescription",
                        "simpleUserMsg"))
                : "incorrectJson";

        ResponseBody responseBody = mock(ResponseBody.class);
        try {
            PowerMockito.doReturn(serverResponse).when(responseBody).string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.error(500, responseBody);
    }

    @Test
    public void testInvalidApiError() {
        TestSubscriber<ApiResponse> subscriber = new TestSubscriber<>();
        Response<ApiResponse> response = getApiResponseResponse(false);

        new ParseErrorResponseFunc<>()
                .setGson(gson)
                .setLogger(logger)
                .call(new HttpException(response))
                .doOnError(errorAction)
                .subscribe(subscriber);


        assertThat(errorAction.error, instanceOf(retrofit2.adapter.rxjava.HttpException.class));

        subscriber.assertTerminalEvent();
        subscriber.assertNotCompleted();
    }

    @Test
    public void testInvalidApiErrorWithoutBody() {
        TestSubscriber<ApiResponse> subscriber = new TestSubscriber<>();
        Response<ApiResponse> response = getApiResponseResponseEmptyBody();

        new ParseErrorResponseFunc<>()
                .setGson(gson)
                .call(new HttpException(response))
                .doOnError(errorAction)
                .subscribe(subscriber);


        assertThat(errorAction.error, instanceOf(HttpException.class));

        subscriber.assertTerminalEvent();
        subscriber.assertNotCompleted();
    }

    @NonNull
    private Response<ApiResponse> getApiResponseResponseEmptyBody() {

        ResponseBody responseBody = mock(ResponseBody.class);
        try {
            PowerMockito.doThrow(new IOException()).when(responseBody).string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.error(401, responseBody);
    }

    static class OnErrorAction implements Action1<Throwable> {
        Throwable error;

        @Override
        public void call(Throwable throwable) {
            error = throwable;
        }
    }
}