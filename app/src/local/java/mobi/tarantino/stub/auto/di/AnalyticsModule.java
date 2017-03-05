package mobi.tarantino.stub.auto.di;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.analytics.GoogleAnalytics;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.AnalyticsScope;

@Module
public class AnalyticsModule {

    @NonNull
    @AnalyticsScope
    @Provides
    public AnalyticReporter provideAnalyticReporter(@NonNull Context context, @NonNull
            IPreferencesManager
            preferencesManager, Logger logger) {
        registerGaClientId(context, preferencesManager);

        AnalyticReporter reporter = new AnalyticReporter(preferencesManager);
        reporter.registerAnalytic(new LocalAnalytic(logger));

        return reporter;
    }

    private void registerGaClientId(@NonNull Context context, @NonNull IPreferencesManager
            preferencesManager) {
        if (TextUtils.isEmpty(preferencesManager.getGaClientId())) {
            preferencesManager.setGaClientId(
                    GoogleAnalytics.getInstance(context).newTracker(Consts.Analytics.TRACKER_ID)
                            .get("&cid"));
        }
    }
}
