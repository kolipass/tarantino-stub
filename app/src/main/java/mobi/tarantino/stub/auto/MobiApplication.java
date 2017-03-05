package mobi.tarantino.stub.auto;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;

import mobi.tarantino.stub.auto.di.BaseComponent;
import mobi.tarantino.stub.auto.di.DependencyComponentManager;

public class MobiApplication extends Application {
    private DependencyComponentManager dependencyComponentManager;

    @NonNull
    public static MobiApplication get(@NonNull Context context) {
        return (MobiApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        LeakCanary.install(this);
//        if (BuildConfig.USE_CRASHLYTICS) {
//            Fabric.with(this, new Answers(), new Crashlytics());
//        }
        initDI();
    }

    private void initDI() {
        dependencyComponentManager = new DependencyComponentManager(this);
    }

    public DependencyComponentManager getComponentContainer() {
        return dependencyComponentManager;
    }

    @NonNull
    public MobiApplication setComponentContainer(DependencyComponentManager componentContainer) {
        this.dependencyComponentManager = componentContainer;
        return this;
    }

    @Deprecated
    public BaseComponent getBaseComponent() {
        return dependencyComponentManager.getBaseComponent();
    }

}
