package mobi.tarantino.stub.auto.feature.dashboard.services.driverAssistance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.di.DependencyComponentManager;
import mobi.tarantino.stub.auto.feature.dashboard.services.OnBeUserVieweble;

import static mobi.tarantino.stub.auto.analytics.Reporter.CATEGORY_HELP;


public class DriverAssistanceLayout extends CardView implements OnBeUserVieweble {
    protected AnalyticReporter analyticReporter;

    public DriverAssistanceLayout(@NonNull Context context) {
        super(context);
    }

    public DriverAssistanceLayout(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DriverAssistanceLayout(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            injectDependencies();
        }
    }

    private void injectDependencies() {
        DependencyComponentManager componentContainer = MobiApplication.get(getContext())
                .getComponentContainer();

        analyticReporter = componentContainer.getAnalyticComponent().provideAnalyticReporter();
    }

    @Override
    public void onVieweble() {
        analyticReporter.widgetEvent(Reporter.SCREEN_SERVICES, CATEGORY_HELP);

    }
}
