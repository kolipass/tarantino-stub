/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.tarantino.stub.auto.retrofitUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final Logger logger;
    private volatile Level level = Level.NONE;

    public HttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public HttpLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small
     * sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    public Level getLevel() {
        return level;
    }

    /**
     * Change the level at which this interceptor logs.
     */
    public HttpLoggingInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        StringBuilder requestMessage = new StringBuilder();

        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' +
                protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        requestMessage.append(requestStartMessage).append("\n");

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor.
                // Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    requestMessage
                            .append("Content-Type: ")
                            .append(requestBody.contentType())
                            .append("\n");
                }
                if (requestBody.contentLength() != -1) {
                    requestMessage
                            .append("Content-Length: ")
                            .append(requestBody.contentLength())
                            .append("\n");
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase
                        (name)) {
                    requestMessage.append(name)
                            .append(": ")
                            .append(headers.value(i))
                            .append("\n");
                }
            }

            if (!logBody || !hasRequestBody) {
                requestMessage.append("--> END ").append(request.method());
            } else if (bodyEncoded(request.headers())) {
                requestMessage.append("--> END ")
                        .append(request.method())
                        .append(" (encoded body omitted)").append("\n");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                requestMessage.append("");
                if (isPlaintext(buffer)) {
                    requestMessage.append(buffer.readString(charset)).append("\n");
                    requestMessage.append("--> END ")
                            .append(request.method())
                            .append(" (")
                            .append(requestBody.contentLength())
                            .append("-byte body)")

                            .append("\n");
                } else {
                    requestMessage.append("--> END ")
                            .append(request.method())
                            .append(" (binary ")
                            .append(requestBody.contentLength())
                            .append("-byte body omitted)")
                            .append("\n");
                }
            }
        }
        logger.log(requestMessage.toString());
        StringBuilder responseMessage = new StringBuilder();

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            responseMessage.append("<-- HTTP FAILED: ")
                    .append(e)
                    .append("\n");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        responseMessage.append("<-- ")
                .append(response.code())
                .append(' ')
                .append(response.message())
                .append(' ')
                .append(response.request().url())
                .append(" (")
                .append(tookMs)
                .append("ms")
                .append(!logHeaders ? ", " + bodySize + " body" : "")
                .append(')')
                .append("\n");

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                responseMessage.append(headers.name(i)).append(": ").append(headers.value(i))
                        .append("\n");
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                responseMessage.append("<-- END HTTP").append("\n");
            } else if (bodyEncoded(response.headers())) {
                responseMessage.append("<-- END HTTP (encoded body omitted)").append("\n");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        responseMessage.append("").append("\n");
                        responseMessage.append("Couldn't decode the response body; charset is " +
                                "likely malformed.").append("\n");
                        responseMessage.append("<-- END HTTP").append("\n");

                        return response;
                    }
                }

                if (!isPlaintext(buffer)) {
                    responseMessage.append("").append("\n");
                    responseMessage.append("<-- END HTTP (binary ").append(buffer.size()).append
                            ("-byte body omitted)").append("\n");
                    return response;
                }

                if (contentLength != 0) {
                    responseMessage.append("").append("\n");
                    responseMessage.append(buffer.clone().readString(charset)).append("\n");
                }

                responseMessage.append("<-- END HTTP (").append(buffer.size()).append("-byte " +
                        "body)").append("\n");
            }
        }
        logger.log(responseMessage.toString());
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };

        void log(String message);
    }
}
