package mobi.tarantino.stub.auto.model.database.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion;

import static mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion.COL_ANSWER_TIME;
import static mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion.COL_CREATED_AT;
import static mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion.COL_ID;
import static mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion.TABLE_NAME;

/**

 */
public class AnswerQuestionDao extends BaseDaoImpl<AnswerQuestion, Integer> {

    public AnswerQuestionDao(ConnectionSource connectionSource, Class<AnswerQuestion> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<AnswerQuestion> getAllAnswerQuestions() {
        try {
            return queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void updateAnswerTime(int id) throws SQLException {
        UpdateBuilder<AnswerQuestion, Integer> builder = updateBuilder();
        builder.where().eq(COL_ID, id);
        builder.updateColumnValue(COL_ANSWER_TIME, new Date().getTime() / 1000);
        builder.update();
    }

    public List<AnswerQuestion> getNoAnsweredQuestionList() throws SQLException {
        return queryBuilder().where().eq(COL_ANSWER_TIME, 0).query();
    }

    public List<AnswerQuestion> getAnsweredQuestionList() throws SQLException {
        return queryBuilder().where().ge(COL_ANSWER_TIME, 1).query();
    }

    public long getLastCreatedTime() throws SQLException {
        String query = String.format("SELECT max(%s) FROM %s", COL_CREATED_AT, TABLE_NAME);
        return queryRawValue(query);
    }

    public void saveList(final List<AnswerQuestion> list) throws SQLException {
        TransactionManager.callInTransaction(getConnectionSource(), new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (AnswerQuestion answerQuestion : list) {
                    createOrUpdate(answerQuestion);
                }
                return null;
            }
        });
    }
}
