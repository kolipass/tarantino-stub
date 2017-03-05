package mobi.tarantino.stub.auto.feature.demoTour;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;
import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.IntentStarter;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.feature.demoTour.pages.FirstTourPage;
import mobi.tarantino.stub.auto.feature.demoTour.pages.SecondTourPage;
import mobi.tarantino.stub.auto.feature.demoTour.pages.ThirdTourPage;

/**

 */

public class DemoTourActivity extends AppCompatActivity implements DemoTour.FinishListener {

    @Nullable
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @NonNull
    @BindView(R.id.circle_indicator)
    CircleIndicator circleIndicator;
    @Inject
    IntentStarter starter;
    @Inject
    IPreferencesManager preferencesManager;
    private Unbinder unbinder;
    @Nullable
    private DemoTour demoTour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_tour);
        unbinder = ButterKnife.bind(this);
        MobiApplication.get(this).getComponentContainer().getBaseComponent().inject(this);
        initTour();
    }

    private void initTour() {
        demoTour = new DemoTour(new DemoTourViewPagerAdapter(this), viewPager);
        demoTour.addDemoPage(new FirstTourPage(this, R.layout.tour_first));
        demoTour.addDemoPage(new SecondTourPage(this, R.layout.tour_second));
        demoTour.addDemoPage(new ThirdTourPage(this, R.layout.tour_third));
        circleIndicator.setViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onFinishDemoTour() {
        finish();
        preferencesManager.demoTourFinish(true);
        starter.start();
    }
}
