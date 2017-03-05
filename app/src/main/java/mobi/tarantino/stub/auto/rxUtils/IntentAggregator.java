package mobi.tarantino.stub.auto.rxUtils;

import android.content.Context;
import android.content.Intent;

/**

 */
public class IntentAggregator {
    private Context context;
    private Intent intent;

    public IntentAggregator(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    public Context getContext() {
        return context;
    }

    public Intent getIntent() {
        return intent;
    }
}
