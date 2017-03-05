package mobi.tarantino.stub.auto.decoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.res.ResourcesCompat;

public abstract class ToolbarColorizer {

    private Resources resources;

    public ToolbarColorizer(Context context) {
        resources = context.getResources();
    }

    protected Drawable getDrawable(int drawableId, Resources.Theme theme) {
        return ResourcesCompat.getDrawable(resources, drawableId, theme);
    }

    protected int getColor(int colorId, Resources.Theme theme) {
        return ResourcesCompat.getColor(resources, colorId, theme);
    }

    @ColorInt
    public abstract int getBackgroundColor();

    @ColorInt
    public abstract int getTitleColor();

    @ColorInt
    public abstract int getSubTitleColor();

    @ColorInt
    public abstract int getIconColor();

    @ColorInt
    public abstract int getStatusBarColor();
}
