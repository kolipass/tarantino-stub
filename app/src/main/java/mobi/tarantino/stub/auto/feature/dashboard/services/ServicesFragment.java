package mobi.tarantino.stub.auto.feature.dashboard.services;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mobi.tarantino.stub.auto.MobiApplication;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.analytics.AnalyticReporter;
import mobi.tarantino.stub.auto.analytics.Reporter;
import mobi.tarantino.stub.auto.commonView.NotifiedScrollView;
import mobi.tarantino.stub.auto.feature.dashboard.ShowDriverAssistanseListener;

import static mobi.tarantino.stub.auto.analytics.Reporter.CATEGORY_HELP;

/**

 */
public class ServicesFragment extends MvpViewStateFragment<ServicesView, ServicesPresenter>
        implements ServicesView {

    @BindView(R.id.phone_call)
    ViewGroup phoneCallViewGroup;

    @BindView(R.id.other_phones_button)
    Button showOtherPhonesButton;

    private Unbinder unbinder;
    private NotifiedScrollView scrollableContent;
    private AnalyticReporter analyticReporter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        scrollableContent = (NotifiedScrollView) inflater.inflate(R.layout.fragment_services,
                container, false);
        return scrollableContent;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        initAnalyticReporter();
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, scrollableContent);

        scrollableContent.setPercentOfVisibility(70);
        scrollableContent.setOnChildViewVisibilityChangedListener(new NotifiedScrollView
                .OnChildViewVisibilityChangedListener() {
            @Override
            public void onChildViewVisibilityChanged(int index, View v, boolean becameVisible) {
                if (v instanceof OnBeUserVieweble && becameVisible) {
                    ((OnBeUserVieweble) v).onVieweble();
                }
            }
        });
    }

    private void initAnalyticReporter() {
        analyticReporter = MobiApplication.get(getContext())
                .getComponentContainer()
                .getAnalyticComponent()
                .provideAnalyticReporter();
    }

    @Override
    public ServicesPresenter createPresenter() {
        return new ServicesPresenter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ((ServicesViewState) viewState).setScrollState(scrollableContent.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public ViewState createViewState() {
        return new ServicesViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        setRetainInstance(true);
    }

    @Override
    public void setScrollState(final int position) {
        scrollableContent.post(new Runnable() {
            @Override
            public void run() {
                if (scrollableContent != null) {
                    scrollableContent.setScrollY(position);
                }

            }
        });
    }

    @OnClick(R.id.phone_call)
    public void onPhoneCallClick(View view) {
        Uri phoneData = Uri.parse("tel:" + getString(R.string.common_emergency_phone));
        Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL, phoneData);
        getActivity().startActivity(phoneCallIntent);
        analyticReporter.phoneCallEvent(Reporter.SCREEN_SERVICES, getString(R.string.emergency));
    }

    @OnClick(R.id.other_phones_button)
    public void onShowOtherPhonesButtonClick() {
        ((ShowDriverAssistanseListener) getActivity()).showDriverAssistance();

        analyticReporter.widgetClickEvent(Reporter.SCREEN_SERVICES, CATEGORY_HELP, "OtherPhones");
    }
}
