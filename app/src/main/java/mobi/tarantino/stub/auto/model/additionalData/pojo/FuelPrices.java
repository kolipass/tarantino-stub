package mobi.tarantino.stub.auto.model.additionalData.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mobi.tarantino.stub.auto.model.ApiResponse;

/**

 */

public class FuelPrices implements ApiResponse, Parcelable, JsonDeserializer<FuelPrices> {

    public static final Creator<FuelPrices> CREATOR = new Creator<FuelPrices>() {
        @Override
        public FuelPrices createFromParcel(Parcel in) {
            return new FuelPrices(in);
        }

        @Override
        public FuelPrices[] newArray(int size) {
            return new FuelPrices[size];
        }
    };
    private List<FuelInfo> ai92;
    private List<FuelInfo> ai95;
    private List<FuelInfo> ai98;
    private List<FuelInfo> diesel;

    public FuelPrices() {
        this.ai92 = new ArrayList<>();
        this.ai95 = new ArrayList<>();
        this.ai98 = new ArrayList<>();
        this.diesel = new ArrayList<>();
    }

    protected FuelPrices(Parcel in) {
        ai92 = in.createTypedArrayList(FuelInfo.CREATOR);
        ai95 = in.createTypedArrayList(FuelInfo.CREATOR);
        ai98 = in.createTypedArrayList(FuelInfo.CREATOR);
        diesel = in.createTypedArrayList(FuelInfo.CREATOR);
    }

    private void addAi92FuelInfo(FuelInfo fuelInfo) {
        ai92.add(fuelInfo);
    }

    private void addAi95FuelInfo(FuelInfo fuelInfo) {
        ai95.add(fuelInfo);
    }

    private void addAi98FuelInfo(FuelInfo fuelInfo) {
        ai98.add(fuelInfo);
    }

    private void addDieselFuelInfo(FuelInfo fuelInfo) {
        diesel.add(fuelInfo);
    }

    public List<FuelInfo> getAi92() {
        return ai92;
    }

    public List<FuelInfo> getAi95() {
        return ai95;
    }

    public List<FuelInfo> getAi98() {
        return ai98;
    }

    public List<FuelInfo> getDiesel() {
        return diesel;
    }

    @Override
    public FuelPrices deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws JsonParseException {
        FuelPrices pojo = new FuelPrices();

        JsonObject jsonObject = json.getAsJsonObject();

        JsonArray ai92jsonArray = jsonObject.getAsJsonArray("ai92");
        for (int i = 0; i < ai92jsonArray.size(); i++) {
            JsonArray arrayObject = ai92jsonArray.get(i).getAsJsonArray();
            pojo.addAi92FuelInfo(new FuelInfo(arrayObject));
        }

        JsonArray ai95jsonArray = jsonObject.getAsJsonArray("ai95");
        for (int i = 0; i < ai95jsonArray.size(); i++) {
            JsonArray arrayObject = ai95jsonArray.get(i).getAsJsonArray();
            pojo.addAi95FuelInfo(new FuelInfo(arrayObject));
        }

        JsonArray ai98jsonArray = jsonObject.getAsJsonArray("ai98");
        for (int i = 0; i < ai98jsonArray.size(); i++) {
            JsonArray arrayObject = ai98jsonArray.get(i).getAsJsonArray();
            pojo.addAi98FuelInfo(new FuelInfo(arrayObject));
        }

        JsonArray dieseljsonArray = jsonObject.getAsJsonArray("diesel");
        for (int i = 0; i < dieseljsonArray.size(); i++) {
            JsonArray arrayObject = dieseljsonArray.get(i).getAsJsonArray();
            pojo.addDieselFuelInfo(new FuelInfo(arrayObject));
        }

        return pojo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(ai92);
        parcel.writeTypedList(ai95);
        parcel.writeTypedList(ai98);
        parcel.writeTypedList(diesel);
    }

    public List<FuelInfo> getFuelInfo(FuelType fuelType) {
        switch (fuelType) {
            case AI_92:
                return ai92;
            case AI_95:
                return ai95;
            case AI_98:
                return ai98;
            case DIESEL:
                return diesel;
            default:
                return ai92;
        }
    }

    public enum FuelType {
        AI_92, AI_95, AI_98, DIESEL
    }

    public static class FuelInfo implements Parcelable {
        public static final Creator<FuelInfo> CREATOR = new Creator<FuelInfo>() {
            @Override
            public FuelInfo createFromParcel(Parcel in) {
                return new FuelInfo(in);
            }

            @Override
            public FuelInfo[] newArray(int size) {
                return new FuelInfo[size];
            }
        };
        private String date;
        private String cost;
        private String trend;

        private FuelInfo(JsonArray array) {
            date = array.get(0).getAsString();
            cost = array.get(1).getAsString();
            trend = array.get(2).getAsString();
        }

        protected FuelInfo(Parcel in) {
            date = in.readString();
            cost = in.readString();
            trend = in.readString();
        }

        public String getDate() {
            return date;
        }

        public String getCost() {
            return cost;
        }

        public String getTrend() {
            return trend;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(date);
            parcel.writeString(cost);
            parcel.writeString(trend);
        }
    }

}
