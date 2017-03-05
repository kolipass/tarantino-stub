package mobi.tarantino.stub.auto.decoration;

import android.content.Context;

import mobi.tarantino.stub.auto.R;

public class MintToolbarColorizer extends ToolbarColorizer {

    public MintToolbarColorizer(Context context) {
        super(context);
    }

    @Override
    public int getBackgroundColor() {
        return getColor(R.color.mint, null);
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
        return getColor(R.color.mintAccent, null);
    }
}
