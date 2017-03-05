package mobi.tarantino.stub.auto.tokenCrypto;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.TestComponentRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EmptyActivityTest {
    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());

    @Rule
    public TestRule chain = RuleChain.outerRule(component);


    @Test
    public void checkPositiveScreenplay() throws Exception {
        Instrumentation instrumentation
                = getInstrumentation();
        Context ctx = instrumentation.getTargetContext();


        IPreferencesManager iPreferencesManager = MobiApplication.get(ctx).getComponentContainer
                ().getBaseComponent().providesPreferencesManager();

        iPreferencesManager.clearAll();

        String token = "token2332";

        iPreferencesManager.setToken(token);

        String token1 = iPreferencesManager.getToken();

        Log.d("token from preferences", " " + token1);

        assertEquals(token, token1);
    }
}