package mobi.tarantino.stub.auto.retrofitUtils;

import android.content.Context;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.Times;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.model.auth.HttpApiException;
import mobi.tarantino.stub.auto.model.auth.pojo.ErrorMobiApiAnswer;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.ErrorHandler;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.HttpApiExceptionStrategy;
import mobi.tarantino.stub.auto.retrofitUtils.errorHandler.HttpExceptionStrategy;
import okhttp3.internal.http.RealResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpException.class)
public class ErrorHandlerTest {
    public static final String INTERNET_ERROR_MESSAGE = "Internet error message";
    private ErrorHandler errorHandler;
    private ErrorHandler.ErrorHandlerListener listener;
    private IntentStarter intentStarter;
    private Context context;

    @Before
    public void setUp() {
        context = mock(Context.class);
        listener = mock(ErrorHandler.ErrorHandlerListener.class);
        intentStarter = mock(IntentStarter.class);
        errorHandler = new ErrorHandler(context, intentStarter);
        errorHandler.setErrorHandlerListener(listener);
        errorHandler.registerStrategy(HttpException.class, new HttpExceptionStrategy());
        errorHandler.registerStrategy(HttpApiException.class, new HttpApiExceptionStrategy());
        when(context.getString(R.string.unavaliable_internet_error))
                .thenReturn(INTERNET_ERROR_MESSAGE);
    }

    @Test
    public void tesHttpApiCriticalError() {
        HttpApiException exception = createHttpApiException(401, "Error", "401");
        errorHandler.handleError(exception);
        verify(intentStarter).logout();

    }

    @Test
    public void tesHttpCriticalError() {
        Response<String> response = Response.error(401, new RealResponseBody(null, null));
        HttpException exception = new HttpException(response);
        errorHandler.handleError(exception);
        verify(intentStarter).logout();

    }

    @Test
    public void testDefaultError() {
        Throwable throwable = new Throwable("Message");
        errorHandler.handleError(throwable);
        verify(listener).onHandleErrorMessage("Message");
    }

    @Test
    public void test_772_XmlProtocolException() {
        HttpApiException exception = createHttpApiException(772, "Error", "772");
        errorHandler.handleError(exception);
        verify(listener).onHandleErrorMessage(anyString());
        verify(intentStarter).logout();
        verify(intentStarter).updateApp();
    }

    @Test
    public void test_862_XmlProtocolException() {
        HttpApiException exception862 = createHttpApiException(862, "Error", "862");
        HttpApiException exception892 = createHttpApiException(892, "Error", "892");
        errorHandler.handleError(exception862);
        errorHandler.handleError(exception892);
        verify(listener, new Times(2)).onHandleErrorMessage(anyString());
        verify(intentStarter, new Times(2)).logout();
    }

    @NonNull
    private HttpApiException createHttpApiException(int code, String message, String errorCode) {
        Response<String> response = Response.error(code, new RealResponseBody(null, null));
        return new HttpApiException(response,
                new ErrorMobiApiAnswer(message, errorCode));
    }


}