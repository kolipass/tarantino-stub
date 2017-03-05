package mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices;

/**

 */
public interface FuelInfoCardView extends MvpLceView<FuelPrices> {
    void setTabState(int position);

    void setLocation(@Nullable String s);
}
