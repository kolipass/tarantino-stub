package mobi.tarantino.stub.auto.feature.dashboard;

import android.support.v4.view.ViewPager;

public interface ViewPageContainer {
    void addOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    void removeOnPageChangeListener(ViewPager.OnPageChangeListener listener);
}
