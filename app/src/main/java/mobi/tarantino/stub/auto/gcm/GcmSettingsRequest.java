package mobi.tarantino.stub.auto.gcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**

 */

public class GcmSettingsRequest {

    @SerializedName("platform")
    @Expose
    private String platform = "ANDROID";

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("udid")
    @Expose
    private String udid;

    public GcmSettingsRequest(String token, String udid) {
        this.token = token;
        this.udid = udid;
    }

    public String getPlatform() {
        return platform;
    }

    public String getToken() {
        return token;
    }

    public String getUdid() {
        return udid;
    }
}
