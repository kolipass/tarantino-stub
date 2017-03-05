package mobi.tarantino.stub.auto.feature.dashboard.driverAssistance;

import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;
import mobi.tarantino.stub.auto.model.additionalData.pojo.DriverAssistanceInfo;
import mobi.tarantino.stub.auto.mvp.MvpLceRxPresenter;

/**

 */
public class DriverAssistancePresenter extends MvpLceRxPresenter<DriverAssistanceView,
        List<DriverAssistanceInfo>> {

    private MobiAdditionalModel model;

    public DriverAssistancePresenter(MobiAdditionalModel model) {
        this.model = model;
    }

    public void load(boolean pullToRefresh) {
        subscribe(model.getDriverAssistanceInfo(), pullToRefresh);
    }
}
