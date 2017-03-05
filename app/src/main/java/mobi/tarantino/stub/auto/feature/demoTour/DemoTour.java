package mobi.tarantino.stub.auto.feature.demoTour;

import android.support.v4.view.ViewPager;
import android.view.View;

import mobi.tarantino.stub.auto.feature.demoTour.pages.BaseTourPage;

/**

 */

public class DemoTour {

    private DemoTourViewPagerAdapter adapter;
    private ViewPager viewPager;
    private FinishListener listener;

    public DemoTour(DemoTourViewPagerAdapter adapter, ViewPager viewPager) {
        this.adapter = adapter;
        this.viewPager = viewPager;
        init();
    }

    public void addDemoPage(BaseTourPage page) {
        adapter.addPage(page);
        adapter.notifyDataSetChanged();
    }

    public void setOnFinishListener(FinishListener listener) {
        this.listener = listener;
    }

    private void init() {
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new DemoTourPageTransformer());
    }

    public interface FinishListener {
        void onFinishDemoTour();
    }

    private class DemoTourPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            BaseTourPage tourPage = adapter.getAssociatedPage(page, viewPager);

            if ((position > 0 && position < 1)
                    || (position < 0 && position > -1))
                tourPage.onEntryAnimation(Math.abs(position));

            if (position == 0) tourPage.animateVisibleOn();

            if (position >= 1 || position <= -1) tourPage.animateVisibleOff();
        }

    }
}
