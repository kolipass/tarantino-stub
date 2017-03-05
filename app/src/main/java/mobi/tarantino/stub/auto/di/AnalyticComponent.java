package mobi.tarantino.stub.auto.di;

import dagger.Component;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.AnalyticsScope;
import mobi.tarantino.stub.auto.feature.article.ArticleActivity;
import mobi.tarantino.stub.auto.feature.article.ArticleDialogFragment;
import mobi.tarantino.stub.auto.gcm.MobiGcmListenerService;
import mobi.tarantino.stub.auto.gcm.gcmHandlerStrategy.AbstractGcmHandlerStrategy;


@Component(dependencies = {BaseComponent.class},
        modules = {AnalyticsModule.class})
@AnalyticsScope
public interface AnalyticComponent {
    AnalyticReporter provideAnalyticReporter();

    void inject(MobiGcmListenerService mobiGcmListenerService);

    void inject(AbstractGcmHandlerStrategy abstractGcmHandlerStrategy);

    void inject(ArticleDialogFragment articleDialogFragment);

    void inject(ArticleActivity articleActivity);
}
