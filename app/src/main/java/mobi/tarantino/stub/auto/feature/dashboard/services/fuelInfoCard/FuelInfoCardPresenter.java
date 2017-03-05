package mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard;

import android.support.annotation.Nullable;

import mobi.tarantino.stub.auto.model.additionalData.MobiAdditionalModel;
import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices;
import mobi.tarantino.stub.auto.mvp.MvpLceRxPresenter;
import rx.Observable;
import rx.functions.Func1;

public class FuelInfoCardPresenter extends MvpLceRxPresenter<FuelInfoCardView, FuelPrices> {

    private final String defaultCity;
    private MobiAdditionalModel model;
    private FuelPrices data;

    public FuelInfoCardPresenter() {
        defaultCity = null;
    }

    public FuelInfoCardPresenter(MobiAdditionalModel model, String defaultCity) {
        this.model = model;
        this.defaultCity = defaultCity;
    }

    public void loadData(boolean pullToRefresh) {
        if (data == null || pullToRefresh) {
            Observable<FuelPrices> fuelPricesObservable = model.getCityInBg()
                    .flatMap(new Func1<String, Observable<FuelPrices>>() {
                        @Override
                        public Observable<FuelPrices> call(String city) {
                            String location = setLocation(city);
                            return model.getFuelPrices(location);
                        }
                    });
            subscribe(fuelPricesObservable, pullToRefresh);
        } else {
            if (isViewAttached() && getView() != null) {
                getView().setData(data);
                getView().showContent();
            }
        }
    }

    @Nullable
    private String setLocation(String city) {
        String location = city == null || city.equals(defaultCity)
                ? city
                : null;

        if (isViewAttached() && getView() != null) {
            getView().setLocation(location);
        }
        return location;
    }

    @Override
    protected void onNext(FuelPrices data) {
        super.onNext(data);
        this.data = data;
    }

}
