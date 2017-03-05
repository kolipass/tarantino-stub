package mobi.tarantino.stub.auto.model.database.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import mobi.tarantino.stub.auto.model.database.dbo.DriverAssistanceInfoDBO;

public class DriverAssistanceDao extends BaseDaoImpl<DriverAssistanceInfoDBO, Integer> {

    public DriverAssistanceDao(ConnectionSource connectionSource, Class<DriverAssistanceInfoDBO>
            dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public DriverAssistanceDao(ConnectionSource connectionSource,
                               @NonNull DatabaseTableConfig<DriverAssistanceInfoDBO> tableConfig)
            throws
            SQLException {
        super(connectionSource, tableConfig);
    }

    public DriverAssistanceDao(Class<DriverAssistanceInfoDBO> dataClass) throws SQLException {
        super(dataClass);
    }

    public void saveOrUpdateList(@NonNull final List<DriverAssistanceInfoDBO> list) throws
            SQLException {
        TransactionManager.callInTransaction(getConnectionSource(), new Callable<Void>() {
            @Nullable
            @Override
            public Void call() throws Exception {
                for (DriverAssistanceInfoDBO dbo : list) {
                    createOrUpdate(dbo);
                }
                return null;
            }
        });
    }
}
