package mobi.tarantino.stub.auto.model.database.dbo;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import mobi.tarantino.stub.auto.model.database.dao.DriverAssistanceDao;

@DatabaseTable(tableName = DriverAssistanceInfoDBO.TABLE_NAME, daoClass = DriverAssistanceDao.class)
public class DriverAssistanceInfoDBO implements DBO {

    public static final String TABLE_NAME = "driver_assistance_info";
    public static final String _ID = BaseColumns._ID;
    public static final String TITLE = "title";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String CREATED_AT = "created_at";


    @DatabaseField(id = true, dataType = DataType.INTEGER, columnName = _ID, index = true,
            generatedId = false)
    private int id;
    @DatabaseField(dataType = DataType.STRING, columnName = TITLE)
    private String title;
    @DatabaseField(dataType = DataType.STRING, columnName = PHONE_NUMBER)
    private String phoneNumber;

    @DatabaseField(dataType = DataType.LONG, columnName = CREATED_AT)
    private long createdAt;

    public String getTitle() {
        return title;
    }

    public DriverAssistanceInfoDBO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public DriverAssistanceInfoDBO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public int getId() {
        return id;
    }

    public DriverAssistanceInfoDBO setId(int id) {
        this.id = id;
        return this;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public DriverAssistanceInfoDBO setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
