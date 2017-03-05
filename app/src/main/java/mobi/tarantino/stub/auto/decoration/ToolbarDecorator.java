package mobi.tarantino.stub.auto.decoration;

import android.support.annotation.StringRes;


public interface ToolbarDecorator {

    void applyToolbarColorizer(ToolbarColorizer toolbarColorizer);

    void setTitle(@StringRes int resId);
}
