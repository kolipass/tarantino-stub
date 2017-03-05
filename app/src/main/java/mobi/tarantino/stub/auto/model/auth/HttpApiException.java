package mobi.tarantino.stub.auto.model.auth;

import mobi.tarantino.stub.auto.model.auth.pojo.ErrorMobiApiAnswer;
import retrofit2.Response;

public class HttpApiException extends RuntimeException {
    private final int code;
    private final String message;
    private final transient Response<?> response;
    private final ErrorMobiApiAnswer apiAnswer;

    public HttpApiException(Response<?> response, ErrorMobiApiAnswer apiAnswer) {
        super(response != null ? "HTTP " + response.code() + " " + response.message() : null);
        if (response != null) {
            this.code = response.code();
            this.message = response.message();
            this.response = response;
        } else {
            this.code = apiAnswer != null ? getCode(apiAnswer) : 0;

            this.message = apiAnswer != null ? apiAnswer.getError() : null;
            this.response = null;
        }
        this.apiAnswer = apiAnswer;
    }

    private static int getCode(ErrorMobiApiAnswer apiAnswer) {
        try {
            return Integer.valueOf(apiAnswer.getErrorCode());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static HttpApiException unauthorized() {
        return new HttpApiException(null, new ErrorMobiApiAnswer("unauthorized", 401));
    }

    /**
     * HTTP status code.
     */
    public int code() {
        return code;
    }

    /**
     * HTTP status message.
     */
    public String message() {
        return message;
    }

    /**
     * The full HTTP response. This may be null if the exception was serialized.
     */
    public Response<?> response() {
        return response;
    }

    public ErrorMobiApiAnswer getApiAnswer() {
        return apiAnswer;
    }
}
