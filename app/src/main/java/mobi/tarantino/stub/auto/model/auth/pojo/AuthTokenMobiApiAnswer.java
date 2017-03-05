package mobi.tarantino.stub.auto.model.auth.pojo;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**

 */

public class AuthTokenMobiApiAnswer extends ErrorMobiApiAnswer {
    public static final Creator<AuthTokenMobiApiAnswer> CREATOR = new
            Creator<AuthTokenMobiApiAnswer>() {
                @NonNull
                @Override
                public AuthTokenMobiApiAnswer createFromParcel(@NonNull Parcel source) {
                    return new AuthTokenMobiApiAnswer(source);
                }

                @NonNull
                @Override
                public AuthTokenMobiApiAnswer[] newArray(int size) {
                    return new AuthTokenMobiApiAnswer[size];
                }
            };
    @SerializedName("phone")
    @Expose
    String phone;
    @SerializedName("expires_n")
    @Expose
    String expiresIn;
    @SerializedName("tokenType")
    @Expose
    String tokenType;
    @SerializedName("access_token")
    @Expose
    String accessToken;


    public AuthTokenMobiApiAnswer() {
    }

    protected AuthTokenMobiApiAnswer(@NonNull Parcel in) {
        super(in);
        this.phone = in.readString();
        this.expiresIn = in.readString();
        this.tokenType = in.readString();
        this.accessToken = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.phone);
        dest.writeString(this.expiresIn);
        dest.writeString(this.tokenType);
        dest.writeString(this.accessToken);
    }

    public String getTokenType() {
        return tokenType;
    }

    @NonNull
    public AuthTokenMobiApiAnswer setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @NonNull
    public AuthTokenMobiApiAnswer setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    @NonNull
    public AuthTokenMobiApiAnswer setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    @NonNull
    public AuthTokenMobiApiAnswer setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "AuthTokenMobiApiAnswer{" +
                "accessToken='" + accessToken + '\'' +
                ", phone='" + phone + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
