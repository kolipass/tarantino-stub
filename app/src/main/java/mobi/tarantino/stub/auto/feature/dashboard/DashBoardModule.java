package mobi.tarantino.stub.auto.feature.dashboard;

import dagger.Module;
import dagger.Provides;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.feature.dashboard.driverAssistance.DriverAssistancePresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard.FuelInfoCardPresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard
        .NotificationCardPresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.quizCard.QuizCardPresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.trafficLawsChangesCard
        .TrafficLawsChangesCardPresenter;
import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;

@Module
public class DashBoardModule {
    private DashBoardActivity activity;

    public DashBoardModule(DashBoardActivity activity) {
        this.activity = activity;
    }

    @DashBoardScope
    @Provides
    public ArticleListener provideArticleListener() {
        return activity;
    }

    @DashBoardScope
    @Provides
    public ShowAddDocumentListener provideShowAddDocument() {
        return activity;
    }

    @DashBoardScope
    @Provides
    public ViewPageContainer provideViewPageContainer() {
        return activity;
    }


    @DashBoardScope
    @Provides
    public DashBoardPresenter provideDashBoardPresenter() {
        return new DashBoardPresenter();
    }

    @DashBoardScope
    @Provides
    public FuelInfoCardPresenter provideFuelInfoCardPresenter(MobiAdditionalModel model) {
        return new FuelInfoCardPresenter(model, activity.getString(R.string.default_city));
    }

    @DashBoardScope
    @Provides
    public TrafficLawsChangesCardPresenter providePresenter(MobiAdditionalModel model) {
        return new TrafficLawsChangesCardPresenter(model);
    }

    @DashBoardScope
    @Provides
    public QuizCardPresenter provideQuizCardPresenter(MobiAdditionalModel model) {
        return new QuizCardPresenter(model);
    }


    @DashBoardScope
    @Provides
    public DriverAssistancePresenter provideDriverAssistancePresenter(MobiAdditionalModel model) {
        return new DriverAssistancePresenter(model);
    }

    @DashBoardScope
    @Provides
    public NotificationCardPresenter provideNotificationCardPresenter(MobiAdditionalModel model) {
        return new NotificationCardPresenter(model);
    }
}
