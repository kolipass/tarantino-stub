package mobi.tarantino.stub.auto.di;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.Consts;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.feature.auth.AuthActivity;
import mobi.tarantino.stub.auto.feature.auth.AuthComponent;
import mobi.tarantino.stub.auto.feature.auth.AuthModule;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardActivity;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardComponent;
import mobi.tarantino.stub.auto.feature.dashboard.DashBoardModule;
import mobi.tarantino.stub.auto.feature.notifications.NotificationsComponent;
import mobi.tarantino.stub.auto.feature.notifications.NotificationsModule;

import static mobi.tarantino.stub.auto.Consts.ScreenComponents.DASHBOARD;
import static mobi.tarantino.stub.auto.Consts.ScreenComponents.NOTIFICATIONS;
import static mobi.tarantino.stub.auto.Consts.ScreenComponents.SESSION;

public class DependencyComponentManager {

    @Inject
    AuthorizationResolver authorizationResolver;
    @Inject
    IntentStarter intentStarter;

    @Inject
    IPreferencesManager preferencesManager;
    private BaseComponent baseComponent;
    @Nullable
    private AuthComponent authComponent;
    private AnalyticComponent analyticComponent;

    private HashMap<Consts.ScreenComponents, AppComponent> screenComponentMap;


    public DependencyComponentManager(MobiApplication mobiApplication) {
        screenComponentMap = new HashMap<>();
        baseComponent = DaggerBaseComponent
                .builder()
                .baseModule(new BaseModule(mobiApplication))
                .build();
        baseComponent.inject(this);
    }

    public void start() {
        intentStarter.start();
    }

    public BaseComponent getBaseComponent() {
        return baseComponent;
    }

    @NonNull
    public DependencyComponentManager setBaseComponent(BaseComponent baseComponent) {
        this.baseComponent = baseComponent;
        return this;
    }

    public AnalyticComponent getAnalyticComponent() {
        if (analyticComponent == null) {
            analyticComponent = DaggerAnalyticComponent.builder()
                    .baseComponent(baseComponent)
                    .analyticsModule(new AnalyticsModule()).build();
        }
        return analyticComponent;
    }

    @NonNull
    public SessionComponent createSessionComponent() {
        if (authorizationResolver.isAuthorized()) {
//            String token = authorizationResolver.getToken();
//            String phone = preferencesManager.getPhone() != null ? preferencesManager.getPhone()
//                    .replaceAll("[^\\d]", "") : null;

            SessionComponent sessionComponent = baseComponent.buildSessionComponent()

                    .build();
            screenComponentMap.put(SESSION, sessionComponent);
        } else {
            intentStarter.showAuthentication();
        }
        return (SessionComponent) screenComponentMap.get(SESSION);
    }

    @NonNull
    public SessionComponent getSessionComponent() {
        if (screenComponentMap.get(SESSION) == null) {
            return createSessionComponent();
        }
        return (SessionComponent) screenComponentMap.get(SESSION);
    }

    @NonNull
    public AuthComponent createAuthComponent(AuthActivity authActivity) {
        authComponent = baseComponent.plus(new AuthModule(authActivity));
        return authComponent;
    }

    public void releaseSessionComponent() {
        screenComponentMap.put(SESSION, null);
    }

    @NonNull
    public AuthComponent getAuthComponent(AuthActivity activity) {
        if (authComponent == null) {
            return createAuthComponent(activity);
        }
        return authComponent;
    }

    @NonNull
    public NotificationsComponent createNotificationsComponent() {
        screenComponentMap.put(NOTIFICATIONS, getBaseComponent().plus(new NotificationsModule()));
        return (NotificationsComponent) screenComponentMap.get(NOTIFICATIONS);
    }

    public void releaseNotificationsComponent() {
        screenComponentMap.put(NOTIFICATIONS, null);
    }

    @NonNull
    public NotificationsComponent getNotificationsComponent() {
        NotificationsComponent component = (NotificationsComponent) screenComponentMap.get
                (NOTIFICATIONS);
        if (component == null) {
            component = createNotificationsComponent();
        }
        return component;
    }

    @NonNull
    public DashBoardComponent createDashBoardComponent(DashBoardActivity activity) {
        screenComponentMap.put(DASHBOARD, getSessionComponent().plusDashBoardComponent(new
                DashBoardModule(activity)));
        return (DashBoardComponent) screenComponentMap.get(DASHBOARD);
    }

    @NonNull
    public DashBoardComponent getDashBoardComponent(DashBoardActivity activity) {
        if (screenComponentMap.get(DASHBOARD) == null) {
            return createDashBoardComponent(activity);
        }
        return (DashBoardComponent) screenComponentMap.get(DASHBOARD);
    }

    public void releaseAuthComponent() {
        authComponent = null;
    }

    public void releaseDashBoardComponent() {
        screenComponentMap.put(DASHBOARD, null);
    }

    public void releaseScreenComponents() {
        if (screenComponentMap != null) {
            screenComponentMap.clear();
        }
    }
}
