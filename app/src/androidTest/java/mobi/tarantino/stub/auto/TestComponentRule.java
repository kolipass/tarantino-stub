package mobi.tarantino.stub.auto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import mobi.tarantino.stub.auto.di.BaseComponent;
import mobi.tarantino.stub.auto.di.BaseTestModule;
import mobi.tarantino.stub.auto.di.DaggerBaseTestComponent;
import mobi.tarantino.stub.auto.di.DependencyComponentManager;
import mobi.tarantino.stub.auto.di.FakeApiModule;
import mobi.tarantino.stub.auto.di.FakeNavigationModule;
import mobi.tarantino.stub.auto.di.ImmediateSchedulersModule;
import mobi.tarantino.stub.auto.model.auth.MobiAuthApi;

/**
 * Test rule that creates and sets a Dagger TestComponent into the application overriding the
 * existing application component.
 * Use this rule in your test case in order for the app to use mock dependencies.
 * It also exposes some of the dependencies so they can be easily accessed from the tests, e.g. to
 * stub mocks etc.
 */
public class TestComponentRule implements TestRule {

    private BaseComponent mTestComponent;
    private Context mContext;

    public TestComponentRule(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public MobiAuthApi getMockMobiApi() {
        return mTestComponent.mobiAuthApi();
    }

    public IntentStarter getMockIntentStarter() {
        return mTestComponent.providesIntentStarter();
    }

    private void setupDaggerTestComponentInApplication() {
        MobiApplication application = MobiApplication.get(mContext);
        mTestComponent = DaggerBaseTestComponent.builder()
                .authApiModule(new FakeApiModule())
                .baseModule(new BaseTestModule(application))
                .schedulersModule(new ImmediateSchedulersModule())
                .navigationModule(new FakeNavigationModule())
                .build();

        application.setComponentContainer(new DependencyComponentManager(application)
                .setBaseComponent(mTestComponent));
    }

    @Nullable
    @Override
    public Statement apply(@NonNull final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    setupDaggerTestComponentInApplication();
                    base.evaluate();
                } finally {
                    mTestComponent = null;
                }
            }
        };
    }

}
