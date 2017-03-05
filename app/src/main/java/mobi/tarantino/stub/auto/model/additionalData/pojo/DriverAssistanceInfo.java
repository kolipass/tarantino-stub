package mobi.tarantino.stub.auto.model.additionalData.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**

 */

public class DriverAssistanceInfo implements Parcelable {
    public static final Creator<DriverAssistanceInfo> CREATOR =
            new Creator<DriverAssistanceInfo>() {
                @NonNull
                @Override
                public DriverAssistanceInfo createFromParcel(@NonNull Parcel in) {
                    return new DriverAssistanceInfo(in);
                }

                @NonNull
                @Override
                public DriverAssistanceInfo[] newArray(int size) {
                    return new DriverAssistanceInfo[size];
                }
            };
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("number")
    @Expose
    private String phoneNumber;
    @SerializedName("created_at")
    @Expose
    private long createdAt;

    public DriverAssistanceInfo() {
    }

    protected DriverAssistanceInfo(@NonNull Parcel in) {
        id = in.readInt();
        title = in.readString();
        phoneNumber = in.readString();
        createdAt = in.readLong();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @NonNull
    public DriverAssistanceInfo setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public DriverAssistanceInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public DriverAssistanceInfo setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public DriverAssistanceInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(phoneNumber);
        dest.writeLong(createdAt);
    }
}
