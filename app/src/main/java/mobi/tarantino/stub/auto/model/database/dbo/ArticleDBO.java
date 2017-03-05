package mobi.tarantino.stub.auto.model.database.dbo;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.model.database.dao.ArticleDao;

@DatabaseTable(tableName = ArticleDBO.TABLE_NAME, daoClass = ArticleDao.class)
public class ArticleDBO implements DBO, Parcelable {

    public static final String TYPE_LAW = "law";
    public static final String TYPE_PARTNER_ACTIONS = "partner_actions";
    public static final String TABLE_NAME = "articles";
    public static final String COL_ID = BaseColumns._ID;
    public static final String COL_URL = "url";
    public static final String COL_TITLE = "title";
    public static final String COL_PREVIEW = "preview";
    public static final String COL_TYPE = "type";
    public static final String VIEWED = "viewed";
    public static final String CREATION_DATE = "creation_date";
    public static final Parcelable.Creator<ArticleDBO> CREATOR = new Parcelable
            .Creator<ArticleDBO>() {
        @NonNull
        @Override
        public ArticleDBO createFromParcel(@NonNull Parcel source) {
            return new ArticleDBO(source);
        }

        @NonNull
        @Override
        public ArticleDBO[] newArray(int size) {
            return new ArticleDBO[size];
        }
    };
    @DatabaseField(columnName = COL_ID, dataType = DataType.INTEGER, id = true)
    private int id;
    @DatabaseField(columnName = COL_URL, dataType = DataType.STRING)
    private String url;
    @DatabaseField(columnName = COL_TITLE, dataType = DataType.STRING)
    private String title;
    @DatabaseField(columnName = COL_PREVIEW, dataType = DataType.STRING)
    private String preview;
    @DatabaseField(columnName = VIEWED, dataType = DataType.BOOLEAN)
    private boolean viewed;
    @DatabaseField(columnName = COL_TYPE, dataType = DataType.STRING)
    private String type;
    @DatabaseField(columnName = CREATION_DATE, dataType = DataType.LONG)
    private long creationDate;

    public ArticleDBO() {
    }

    public ArticleDBO(int id, String url, String title, String preview, String type) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.preview = preview;
        this.type = type;
    }

    protected ArticleDBO(@NonNull Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
        this.title = in.readString();
        this.preview = in.readString();
        this.viewed = in.readByte() != 0;
        this.type = in.readString();
        this.creationDate = in.readLong();
    }

    @NonNull
    public static ArticleDBO createOrUpdate(@Nullable ArticleDBO articleDBO, @NonNull Article
            article) {
        if (articleDBO == null) {
            articleDBO = new ArticleDBO();
        }
        articleDBO.setId(article.getId())
                .setPreview(article.getPreview())
                .setTitle(article.getTitle())
                .setUrl(article.getUrl())
                .setCreationDate(article.getCreatedAt());

        return articleDBO;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public ArticleDBO setId(int id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    @NonNull
    public ArticleDBO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public ArticleDBO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPreview() {
        return preview;
    }

    @NonNull
    public ArticleDBO setPreview(String preview) {
        this.preview = preview;
        return this;
    }

    public boolean isViewed() {
        return viewed;
    }

    @NonNull
    public ArticleDBO setViewed(boolean viewed) {
        this.viewed = viewed;
        return this;
    }

    public String getType() {
        return type;
    }

    @NonNull
    public ArticleDBO setType(String type) {
        this.type = type;
        return this;
    }

    public long getCreationDate() {
        return creationDate;
    }

    @NonNull
    public ArticleDBO setCreationDate(long creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.preview);
        dest.writeByte(this.viewed ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeLong(this.creationDate);
    }
}
