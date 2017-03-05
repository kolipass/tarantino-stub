package mobi.tarantino.stub.auto.model.additionalData.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import mobi.tarantino.stub.auto.model.ApiResponse;

public class Question implements ApiResponse, Parcelable {

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @NonNull
        @Override
        public Question createFromParcel(@NonNull Parcel source) {
            return new Question(source);
        }

        @NonNull
        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("yes_url")
    @Expose
    private String yesUrl;

    @SerializedName("no_url")
    @Expose
    private String noUrl;

    @SerializedName("created_at")
    @Expose
    private long createAtTime;

    public Question() {
    }

    public Question(int id, String content, String yesUrl, String noUrl) {
        this.id = id;
        this.content = content;
        this.yesUrl = yesUrl;
        this.noUrl = noUrl;
    }

    protected Question(@NonNull Parcel in) {
        this.content = in.readString();
        this.yesUrl = in.readString();
        this.noUrl = in.readString();
        this.id = in.readInt();
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return The yesUrl
     */
    public String getYesUrl() {
        return yesUrl;
    }

    /**
     * @param yesUrl The yes_url
     */
    public void setYesUrl(String yesUrl) {
        this.yesUrl = yesUrl;
    }

    /**
     * @return The noUrl
     */
    public String getNoUrl() {
        return noUrl;
    }

    /**
     * @param noUrl The no_url
     */
    public void setNoUrl(String noUrl) {
        this.noUrl = noUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.yesUrl);
        dest.writeString(this.noUrl);
        dest.writeInt(this.id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "content='" + content + '\'' +
                ", yesUrl='" + yesUrl + '\'' +
                ", noUrl='" + noUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createAtTime;
    }

    public void setCreateTime(long createTime) {
        this.createAtTime = createTime;
    }
}
