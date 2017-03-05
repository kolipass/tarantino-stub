package mobi.tarantino.stub.auto;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import mobi.tarantino.stub.auto.retrofitUtils.HttpLoggingInterceptor;


public class Logger implements HttpLoggingInterceptor.Logger {
    private static final String THIS_NAME = Logger.class.getName();
    private static final String CLASS_LOCATION_FORMAT = "%s.%s:%s";
    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_TAG = Logger.class.getSimpleName();

    private static String getLocation() {
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean found = false;

        for (StackTraceElement trace : traces) {
            try {
                if (found) {
                    if (!trace.getClassName().startsWith(THIS_NAME)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return String.format(CLASS_LOCATION_FORMAT, getClassName(clazz), trace
                                .getMethodName(), trace.getLineNumber());
                    }
                } else if (trace.getClassName().startsWith(THIS_NAME)) {
                    found = true;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        return DEFAULT_TAG;
    }

    private static String getClassName(@Nullable Class<?> c) {
        if (c == null)
            return EMPTY_STRING;

        String name = c.getSimpleName();
        return TextUtils.isEmpty(name) ? getClassName(c.getEnclosingClass()) : name;
    }

    public void d(String message) {
        String tag = getLocation();

        Log.println(Log.DEBUG, tag, message);


//        if (BuildConfig.USE_CRASHLYTICS) {
//            Crashlytics.log(message);
//        }
    }

    public void d(@NonNull Bundle bundle) {
        StringBuilder message = new StringBuilder().append('{');
        for (String key : bundle.keySet()) {
            message.append('\"')
                    .append(key)
                    .append('\"')
                    .append(':')
                    .append('\"')
                    .append(bundle.get(key))
                    .append('\"');
        }
        message.append("}");
        d(message.toString());
    }

    @Override
    public void log(String message) {
        d(message);
    }

    public void e(@NonNull Throwable e) {
        e.printStackTrace();
    }
}
