package mobi.tarantino.stub.auto.di;

import android.test.mock.MockContext;


public class Util {
    public static BaseComponent getFakeComponent() {
        return DaggerBaseComponent
                .builder()
                .baseModule(new BaseTestModule(new MockContext()))
                .schedulersModule(new ImmediateSchedulersModule())
                .authApiModule(new FakeApiModule())
                .navigationModule(new FakeNavigationModule())
                .build();
    }

    public static BaseComponent getRealComponent() {
        return DaggerBaseTestComponent
                .builder()
                .baseModule(new BaseTestModule(new MockContext()))
                .schedulersModule(new ImmediateSchedulersModule())
                .build();
    }

}
