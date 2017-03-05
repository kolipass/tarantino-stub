package mobi.tarantino.stub.auto.feature.dashboard.services.fuelInfoCard;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.AbsParcelableLceViewState;

import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices;

public class FuelInfoCardViewState extends AbsParcelableLceViewState<FuelPrices, FuelInfoCardView> {

    public static final Creator<FuelInfoCardViewState> CREATOR = new
            Creator<FuelInfoCardViewState>() {
                @Override
                public FuelInfoCardViewState createFromParcel(Parcel in) {
                    return new FuelInfoCardViewState(in);
                }

                @Override
                public FuelInfoCardViewState[] newArray(int size) {
                    return new FuelInfoCardViewState[size];
                }
            };
    private int tabState;
    private boolean defaultCity;

    public FuelInfoCardViewState() {
    }

    protected FuelInfoCardViewState(Parcel in) {
        readFromParcel(in);
    }

    public void saveTabState(int tabState) {
        this.tabState = tabState;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(loadedData, i);
        parcel.writeInt(tabState);
        parcel.writeByte(this.defaultCity ? (byte) 1 : (byte) 0);
        super.writeToParcel(parcel, i);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        loadedData = in.readParcelable(getClassLoader());
        tabState = in.readInt();
        defaultCity = in.readByte() == 1;
        super.readFromParcel(in);
    }

    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public void apply(FuelInfoCardView view, boolean retained) {
        super.apply(view, retained);
        view.setTabState(tabState);
    }

    public boolean isDefaultCity() {
        return defaultCity;
    }

    public void setDefaultCity(boolean defaultCity) {
        this.defaultCity = defaultCity;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
