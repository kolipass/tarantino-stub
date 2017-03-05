package mobi.tarantino.stub.auto.feature.dashboard;

import dagger.Subcomponent;
import mobi.tarantino.stub.auto.di.AppComponent;
import mobi.tarantino.stub.auto.feature.article.ArticleDialogFragment;
import mobi.tarantino.stub.auto.feature.dashboard.driverAssistance.DriverAssistanceFragment;
import mobi.tarantino.stub.auto.feature.dashboard.driverAssistance.DriverAssistancePresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard.FuelInfoCardPresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard.NotificationCardLayout;
import mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard
        .NotificationCardPresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.quizCard.QuizCardPresenter;
import mobi.tarantino.stub.auto.feature.dashboard.services.trafficLawsChangesCard
        .TrafficLawsChangesCardLayout;
import mobi.tarantino.stub.auto.feature.dashboard.services.trafficLawsChangesCard
        .TrafficLawsChangesCardPresenter;


@Subcomponent(modules = DashBoardModule.class)
@DashBoardScope
public interface DashBoardComponent extends AppComponent {

    void inject(DashBoardActivity activity);


    void inject(TrafficLawsChangesCardLayout trafficLawsChangesCardLayout);


    void inject(ArticleDialogFragment articleDialogFragment);


    void inject(DriverAssistanceFragment driverAssistanceFragment);

    void inject(NotificationCardLayout notificationCardLayout);


    FuelInfoCardPresenter fuelInfoCardPresenter();

    TrafficLawsChangesCardPresenter trafficLawsChangesCardPresenter();

    QuizCardPresenter quizCardPresenter();


    DriverAssistancePresenter driverAssistancePresenter();

    NotificationCardPresenter notificationCardPresenter();
}