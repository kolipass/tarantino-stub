package mobi.tarantino.stub.auto.model.auth.pojo;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**

 */

public class AuthCodeMobiApiAnswer extends ErrorMobiApiAnswer {
    public static final Creator<AuthCodeMobiApiAnswer> CREATOR = new
            Creator<AuthCodeMobiApiAnswer>() {
                @Override
                public AuthCodeMobiApiAnswer createFromParcel(Parcel source) {
                    return new AuthCodeMobiApiAnswer(source);
                }

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

    protected AuthCodeMobiApiAnswer(Parcel in) {
        super(in);
        this.code = in.readString();
    }

    public String getCode() {
        return code;
    }

    public AuthCodeMobiApiAnswer setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
    }
}
