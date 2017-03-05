package mobi.tarantino.stub.auto.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import mobi.tarantino.stub.auto.Logger;
import mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;
import mobi.tarantino.stub.auto.model.database.dbo.DBO;
import mobi.tarantino.stub.auto.model.database.dbo.DriverAssistanceInfoDBO;

import static mobi.tarantino.stub.auto.Consts.Database.DB_NAME;
import static mobi.tarantino.stub.auto.Consts.Database.DB_VERSION;
import static mobi.tarantino.stub.auto.model.database.DatabaseHelper.DaoImpl.ARTICLE;
import static mobi.tarantino.stub.auto.model.database.DatabaseHelper.DaoImpl.DRIVER_ASSISTANCE_INFO;

/**

 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            for (DaoImpl dao : DaoImpl.values()) {
                TableUtils.createTable(getConnectionSource(), dao.dbo);
            }
        } catch (SQLException e) {
            Logger.DEFAULT.log("Create DB error");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int
            oldVersion, int newVersion) {
        try {
            if (oldVersion == 1 && newVersion == 2) {
                TableUtils.createTable(getConnectionSource(), DRIVER_ASSISTANCE_INFO.dbo);
            } else if (oldVersion == 2 && newVersion == 3) {
                BaseDaoImpl dao = getDaoImplementation(ARTICLE);
                dao
                        .executeRaw("ALTER TABLE `"
                                + ArticleDBO.TABLE_NAME +
                                "` ADD COLUMN `"
                                + ArticleDBO.VIEWED +
                                "` BOOLEAN DEFAULT 0;");
                dao
                        .executeRaw("ALTER TABLE `"
                                + ArticleDBO.TABLE_NAME +
                                "` ADD COLUMN `"
                                + ArticleDBO.CREATION_DATE +
                                "` STRING;");
            } else {
                for (DaoImpl dao : DaoImpl.values()) {
                    TableUtils.dropTable(getConnectionSource(), dao.dbo, true);
                }
                onCreate(database, connectionSource);

            }
        } catch (SQLException e) {
            Logger.DEFAULT.log("Upgrade DB error");
            e.printStackTrace();
        }
    }

    public BaseDaoImpl getDaoImplementation(DaoImpl implementation) {
        try {
            return DaoManager.createDao(getConnectionSource(), implementation.dbo);
        } catch (SQLException e) {
            Logger.DEFAULT.log("dao implementation create error");
            e.printStackTrace();
        }
        return null;
    }

    public void clearDB() {
        try {
            for (DaoImpl dao : DaoImpl.values()) {
                TableUtils.clearTable(getConnectionSource(), dao.dbo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public enum DaoImpl {
        ANSWER_QUESTION(AnswerQuestion.class),
        ARTICLE(ArticleDBO.class),
        DRIVER_ASSISTANCE_INFO(DriverAssistanceInfoDBO.class);

        private Class<? extends DBO> dbo;

        DaoImpl(Class<? extends DBO> dbo) {
            this.dbo = dbo;
        }

        public Class<? extends DBO> getDbo() {
            return dbo;
        }
    }
}
