package mobi.tarantino.stub.auto.model.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**

 */
public class DatabaseHelperFactory {

    private DatabaseHelper helper;
    private Context context;

    public DatabaseHelperFactory(Context context) {
        this.context = context;
        setHelper(context);
    }

    public DatabaseHelper getHelper() {
        return helper;
    }

    public void setHelper(Context context) {
        helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public void releaseHelper() {
        OpenHelperManager.releaseHelper();
    }
}
