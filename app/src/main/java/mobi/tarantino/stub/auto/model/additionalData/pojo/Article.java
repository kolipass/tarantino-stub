package mobi.tarantino.stub.auto.model.additionalData.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import mobi.tarantino.stub.auto.model.ApiResponse;

/**

 */

public class Article implements ApiResponse, Parcelable, Comparable<Article> {

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @NonNull
        @Override
        public Article createFromParcel(@NonNull Parcel in) {
            return new Article(in);
        }

        @NonNull
        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("created_at")
    @Expose
    private long createdAt;
    @SerializedName("id")
    @Expose
    private int id;

    public Article() {
    }

    protected Article(@NonNull Parcel in) {
        url = in.readString();
        title = in.readString();
        preview = in.readString();
        createdAt = in.readLong();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(preview);
        dest.writeLong(createdAt);
        dest.writeInt(id);
    }

    public long getCreatedAt() {

        return createdAt;
    }

    @NonNull
    public Article setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public Article setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    @NonNull
    public Article setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    @NonNull
    public Article setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @return The preview
     */
    public String getPreview() {
        return preview;
    }

    /**
     * @param preview The preview
     */
    @NonNull
    public Article setPreview(String preview) {
        this.preview = preview;
        return this;
    }

    @Override
    public int compareTo(@NonNull Article o) {
        return (createdAt < o.createdAt) ? 1 : ((createdAt == o.createdAt) ? 0 : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
