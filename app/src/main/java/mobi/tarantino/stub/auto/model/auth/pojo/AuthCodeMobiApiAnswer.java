package mobi.tarantino.stub.auto.model.auth.pojo;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**

 */

public class AuthCodeMobiApiAnswer extends ErrorMobiApiAnswer {
    public static final Creator<AuthCodeMobiApiAnswer> CREATOR = new
            Creator<AuthCodeMobiApiAnswer>() {
                @NonNull
                @Override
                public AuthCodeMobiApiAnswer createFromParcel(@NonNull Parcel source) {
                    return new AuthCodeMobiApiAnswer(source);
                }

                @NonNull
                @Override
                public AuthCodeMobiApiAnswer[] newArray(int size) {
                    return new AuthCodeMobiApiAnswer[size];
                }
            };
    @SerializedName("code")
    @Expose
    String code;

    public AuthCodeMobiApiAnswer() {
    }

    protected AuthCodeMobiApiAnswer(@NonNull Parcel in) {
        super(in);
        this.code = in.readString();
    }

    public String getCode() {
        return code;
    }

    @NonNull
    public AuthCodeMobiApiAnswer setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
    }
}
