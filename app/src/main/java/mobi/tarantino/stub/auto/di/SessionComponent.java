package mobi.tarantino.stub.auto.di;

import dagger.Subcomponent;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardComponent;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardModule;

@SessionScope
@Subcomponent(
        modules = {}
)
public interface SessionComponent extends AppComponent {

    DashBoardComponent plusDashBoardComponent(DashBoardModule dashBoardModule);


    @Subcomponent.Builder
    interface Builder {

        SessionComponent build();
    }
}
