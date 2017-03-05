package mobi.tarantino.stub.auto.model.database.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;


public class ArticleDao extends BaseDaoImpl<ArticleDBO, Integer> {


    public ArticleDao(ConnectionSource connectionSource, Class<ArticleDBO> dataClass) throws
            SQLException {
        super(connectionSource, dataClass);
    }

    public List<ArticleDBO> getAll() throws SQLException {
        return queryForAll();
    }

    public List<ArticleDBO> getAllPartnerActions() throws SQLException {
        return queryBuilder().where().eq(ArticleDBO.COL_TYPE, ArticleDBO.TYPE_PARTNER_ACTIONS)
                .query();
    }

    public List<ArticleDBO> getAllLaws() throws SQLException {
        return queryBuilder().where().eq(ArticleDBO.COL_TYPE, ArticleDBO.TYPE_LAW).query();
    }

    public int removeArticle(ArticleDBO articleDBO) throws SQLException {
        return delete(articleDBO);
    }

    public void removeAllPartnerActions() throws SQLException {
        deleteBuilder().where().eq(ArticleDBO.COL_TYPE, ArticleDBO.TYPE_PARTNER_ACTIONS);
        deleteBuilder().delete();
    }

    public void removeAllNotification() throws SQLException {
        deleteBuilder().delete();
    }

    public void removeAllLaws() throws SQLException {
        deleteBuilder().where().eq(ArticleDBO.COL_TYPE, ArticleDBO.TYPE_LAW);
        deleteBuilder().delete();
    }

    public void saveArticle(ArticleDBO articleDBO) throws SQLException {
        create(articleDBO);
    }

    public void createOrUpdateList(@NonNull final Collection<ArticleDBO> collection) throws
            SQLException {
        TransactionManager.callInTransaction(getConnectionSource(), new Callable<Void>() {
            @Nullable
            @Override
            public Void call() throws Exception {
                for (ArticleDBO articleDBO : collection) {
                    createOrUpdate(articleDBO);
                }
                return null;
            }
        });
    }
}
