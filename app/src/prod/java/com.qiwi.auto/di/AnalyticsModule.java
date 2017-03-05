package mobi.tarantino.stub.auto.di;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.AnalyticsScope;
import mobi.tarantino.stub.auto.analytics.MobiFabricAnalytic;
import mobi.tarantino.stub.auto.analytics.MobiGoogleAnalytic;


@Module
public class AnalyticsModule {

    @AnalyticsScope
    @Provides
    public AnalyticReporter provideAnalyticReporter(Context context, IPreferencesManager
            preferencesManager) {
        registerGaClientId(context, preferencesManager);

        AnalyticReporter reporter = new AnalyticReporter(preferencesManager);

        TagManager tagManager = TagManager.getInstance(context);
        tagManager.setVerboseLoggingEnabled(true);
        DataLayer dataLayer = tagManager.getDataLayer();
        reporter.registerAnalytic(new MobiGoogleAnalytic(dataLayer));
        reporter.registerAnalytic(new MobiFabricAnalytic());

        return reporter;
    }

    private void registerGaClientId(Context context, IPreferencesManager preferencesManager) {
        if (TextUtils.isEmpty(preferencesManager.getGaClientId())) {
            preferencesManager.setGaClientId(
                    GoogleAnalytics.getInstance(context).newTracker(Consts.Analytics.TRACKER_ID)
                            .get("&cid"));
        }
    }
}
