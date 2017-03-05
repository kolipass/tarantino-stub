package mobi.tarantino.stub.auto.model.database.dbo;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mobi.tarantino.stub.auto.model.database.dao.AnswerQuestionDao;

/**

 */
@DatabaseTable(tableName = AnswerQuestion.TABLE_NAME, daoClass = AnswerQuestionDao.class)
public class AnswerQuestion implements DBO {

    public static final String TABLE_NAME = "answer_question";
    public static final String COL_ID = BaseColumns._ID;
    public static final String COL_CONTENT = "content";
    public static final String COL_YES_URL = "yes_url";
    public static final String COL_NO_URL = "no_url";
    public static final String COL_ANSWER_TIME = "answer_time";
    public static final String COL_CREATED_AT = "created_at";

    @DatabaseField(id = true, dataType = DataType.INTEGER, columnName = COL_ID, index = true,
            generatedId = false)
    private int id;

    @DatabaseField(dataType = DataType.STRING, columnName = COL_CONTENT)
    private String content;

    @DatabaseField(dataType = DataType.STRING, columnName = COL_YES_URL)
    private String yesUrl;

    @DatabaseField(dataType = DataType.STRING, columnName = COL_NO_URL)
    private String noUrl;

    /**
     * in seconds
     */
    @DatabaseField(dataType = DataType.LONG, columnName = COL_ANSWER_TIME)
    private long answerTime;

    /**
     * in seconds
     */
    @DatabaseField(dataType = DataType.LONG, columnName = COL_CREATED_AT)
    private long createTime;

    public AnswerQuestion() {
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYesUrl() {
        return yesUrl;
    }

    public void setYesUrl(String yesUrl) {
        this.yesUrl = yesUrl;
    }

    public String getNoUrl() {
        return noUrl;
    }

    public void setNoUrl(String noUrl) {
        this.noUrl = noUrl;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }
}
