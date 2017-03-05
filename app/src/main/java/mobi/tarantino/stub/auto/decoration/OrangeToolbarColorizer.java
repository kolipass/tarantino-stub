package mobi.tarantino.stub.auto.decoration;

import android.content.Context;
import android.support.annotation.NonNull;

import mobi.tarantino.stub.auto.R;

public class OrangeToolbarColorizer extends ToolbarColorizer {

    public OrangeToolbarColorizer(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getBackgroundColor() {
        return getColor(R.color.yellowish_orange, null);
    }

    @Override
    public int getTitleColor() {
        return getColor(R.color.white, null);
    }

    @Override
    public int getSubTitleColor() {
        return getColor(R.color.white, null);
    }

    @Override
    public int getIconColor() {
        return getColor(R.color.white, null);
    }

    @Override
    public int getStatusBarColor() {
        return getColor(R.color.yellow_dark, null);
    }
}
