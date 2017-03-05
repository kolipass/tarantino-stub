package mobi.tarantino.stub.auto.feature.demoTour;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mobi.tarantino.stub.auto.feature.demoTour.pages.BaseTourPage;

/**

 */

public class DemoTourViewPagerAdapter extends PagerAdapter {

    @NonNull
    private ArrayList<BaseTourPage> pageList = new ArrayList<>();
    @Nullable
    private DemoTour.FinishListener finishListener;


    public DemoTourViewPagerAdapter(@Nullable DemoTour.FinishListener listener) {
        this.finishListener = listener;
    }

    public void addPage(@NonNull BaseTourPage page) {
        page.setOnFinishListener(finishListener);
        pageList.add(page);
    }

    public View getView(int position, ViewPager pager) {
        BaseTourPage page;
        page = pageList.get(position);
        return page.getPageView(pager);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Nullable
    public BaseTourPage getAssociatedPage(View view, ViewGroup container) {
        for (BaseTourPage page : pageList) {
            if (page != null) {
                if (isViewFromObject(view, page.getPageView(container))) return page;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewPager pager = (ViewPager) container;
        View view = getView(position, pager);

        pager.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
}
