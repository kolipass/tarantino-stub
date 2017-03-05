package mobi.tarantino.stub.auto.feature.demoTour.pages;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.tarantino.stub.auto.feature.demoTour.DemoTour;

/**

 */

public class BaseTourPage {

    private View page;
    private Context context;
    private int layout;
    private DemoTour.FinishListener listener;

    public BaseTourPage(Context context, @LayoutRes int layout) {
        this.context = context;
        this.layout = layout;
    }

    public void setOnFinishListener(@Nullable DemoTour.FinishListener listener) {
        this.listener = listener;
    }

    public void finish() {
        if (listener != null) listener.onFinishDemoTour();
    }

    public void initViews(View parent) {

    }

    public Context getContext() {
        return context;
    }


    public View inflatePageView(ViewGroup container) {
        return LayoutInflater.from(context).inflate(layout, container, false);
    }

    public View getPageView(ViewGroup container) {
        if (page == null) {
            page = inflatePageView(container);
            initViews(page);
        }
        return page;
    }

    public void animateVisibleOn() {
    }


    public void animateVisibleOff() {

    }


    public void onEntryAnimation(float position) {
    }


    public void onExitAnimation(float position) {
    }
}
