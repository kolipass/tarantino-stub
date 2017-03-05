package mobi.tarantino.stub.auto.model.additionalData;

import android.util.SparseArray;

import com.j256.ormlite.dao.BaseDaoImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.model.AbstractModel;
import mobi.tarantino.stub.auto.model.database.DatabaseHelperFactory;
import mobi.tarantino.stub.auto.model.database.dao.AnswerQuestionDao;
import mobi.tarantino.stub.auto.model.database.dao.ArticleDao;
import mobi.tarantino.stub.auto.model.database.dao.DriverAssistanceDao;
import mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;
import mobi.tarantino.stub.auto.model.database.dbo.DriverAssistanceInfoDBO;
import rx.Observable;
import rx.functions.Action1;

import static mobi.tarantino.stub.auto.model.database.DatabaseHelper.DaoImpl;

/**

 */
public class MobiAdditionalDatabaseModel extends AbstractModel {

    private IPreferencesManager preferencesManager;
    private DatabaseHelperFactory databaseHelperFactory;

    @Inject
    public MobiAdditionalDatabaseModel(DatabaseHelperFactory databaseHelperFactory,
                                       IPreferencesManager preferencesManager) {
        this.databaseHelperFactory = databaseHelperFactory;
        this.preferencesManager = preferencesManager;
    }

    @Inject
    public void setPreferencesManager(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    @Inject
    public void setDatabaseHelperFactory(DatabaseHelperFactory databaseHelperFactory) {
        this.databaseHelperFactory = databaseHelperFactory;
    }

    public SparseArray<AnswerQuestion> getAnsweredQuestionMap() {
        List<AnswerQuestion> list = new ArrayList<>();
        try {
            AnswerQuestionDao answerQuestionDao = (AnswerQuestionDao) getDaoImplementation
                    (DaoImpl.ANSWER_QUESTION);
            list = answerQuestionDao.getAnsweredQuestionList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SparseArray<AnswerQuestion> map = new SparseArray<>();
        for (AnswerQuestion answerQuestion : list) {
            map.append(answerQuestion.getId(), answerQuestion);
        }
        return map;
    }

    public List<AnswerQuestion> getNoAnsweredQuestionList() {
        try {
            AnswerQuestionDao answerQuestionDao = (AnswerQuestionDao) getDaoImplementation
                    (DaoImpl.ANSWER_QUESTION);
            return answerQuestionDao.getNoAnsweredQuestionList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Observable<List<AnswerQuestion>> getNoAnsweredQuestionListObservable() {
        return Observable.just(getNoAnsweredQuestionList());
    }

    public int saveAnswerQuestion(AnswerQuestion answerQuestion) {
        int id = -1;
        try {
            AnswerQuestionDao answerQuestionDao =
                    (AnswerQuestionDao) getDaoImplementation(DaoImpl.ANSWER_QUESTION);
            id = answerQuestionDao.create(answerQuestion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void setAnswerTime(int id) {
        try {
            AnswerQuestionDao answerQuestionDao =
                    (AnswerQuestionDao) getDaoImplementation(DaoImpl.ANSWER_QUESTION);
            answerQuestionDao.updateAnswerTime(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getLastCreatedAnswerTime() {
        try {
            AnswerQuestionDao answerQuestionDao =
                    (AnswerQuestionDao) getDaoImplementation(DaoImpl.ANSWER_QUESTION);
            return answerQuestionDao.getLastCreatedTime();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void saveAnswerQuestionList(List<AnswerQuestion> list) {
        try {
            AnswerQuestionDao answerQuestionDao =
                    (AnswerQuestionDao) getDaoImplementation(DaoImpl.ANSWER_QUESTION);
            answerQuestionDao.saveList(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(List<AnswerQuestion> list) {
        try {
            AnswerQuestionDao answerQuestionDao =
                    (AnswerQuestionDao) getDaoImplementation(DaoImpl.ANSWER_QUESTION);
            answerQuestionDao.saveList(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveEvent(ArticleDBO articleDBO) {
        articleDBO.setType(ArticleDBO.TYPE_PARTNER_ACTIONS);
        try {
            ArticleDao articleDao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
            articleDao.saveArticle(articleDBO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveLaw(ArticleDBO articleDBO) {
        articleDBO.setType(ArticleDBO.TYPE_LAW);
        try {
            ArticleDao articleDao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
            articleDao.saveArticle(articleDBO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Observable<List<ArticleDBO>> getEventListObservable() {
        return Observable.just(getAllEvents());
    }

    public List<ArticleDBO> getAllEvents() {
        ArticleDao articleDao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
        List<ArticleDBO> articleDBOList = new ArrayList<>();
        try {
            articleDBOList = articleDao.getAllPartnerActions();
        } catch (SQLException e) {
            return articleDBOList;
        }
        return articleDBOList;
    }

    public Map<Integer, ArticleDBO> getAllPartnerActionsMap() {
        ArticleDao articleDao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
        List<ArticleDBO> articleDBOList = new ArrayList<>();
        try {
            articleDBOList = articleDao.getAllPartnerActions();
        } catch (SQLException e) {
        }

        Map<Integer, ArticleDBO> map = new HashMap<>();
        for (ArticleDBO articleDBO : articleDBOList) {
            map.put(articleDBO.getId(), articleDBO);
        }
        return map;
    }

    public Map<Integer, ArticleDBO> getAllLawsMap() {
        ArticleDao articleDao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
        List<ArticleDBO> articleDBOList = new ArrayList<>();
        try {
            articleDBOList = articleDao.getAllLaws();
        } catch (SQLException e) {
        }

        Map<Integer, ArticleDBO> map = new HashMap<>();
        for (ArticleDBO articleDBO : articleDBOList) {
            map.put(articleDBO.getId(), articleDBO);
        }
        return map;
    }


    public Observable<List<ArticleDBO>> getLawListObservable() {
        return Observable.just(getAllLaws());
    }

    public List<ArticleDBO> getAllLaws() {
        ArticleDao articleDao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
        List<ArticleDBO> articleDBOList = new ArrayList<>();
        try {
            articleDBOList = articleDao.getAllLaws();
        } catch (SQLException e) {
            return articleDBOList;
        }
        return articleDBOList;
    }

    public void removeArticle(ArticleDBO articleDBO) {
        try {
            ((ArticleDao) getDaoImplementation(DaoImpl.ARTICLE)).removeArticle(articleDBO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Action1<? super List<ArticleDBO>> updateArticles() {
        return new Action1<List<ArticleDBO>>() {
            @Override
            public void call(List<ArticleDBO> articleDBOs) {
                ArticleDao articleDao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
                try {
                    articleDao.createOrUpdateList(articleDBOs);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public void clearAllNotification() {
        try {
            ((ArticleDao) getDaoImplementation(DaoImpl.ARTICLE)).removeAllNotification();
            preferencesManager.setNewFinesCount(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private BaseDaoImpl getDaoImplementation(DaoImpl implementation) {
        return databaseHelperFactory.getHelper().getDaoImplementation(implementation);
    }

    public void deleteArticleById(int id) {
        try {
            getDaoImplementation(DaoImpl.ARTICLE).deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markViewedArticle(ArticleDBO articleDBO) {
        try {
            articleDBO.setViewed(true);
            ArticleDao dao = (ArticleDao) getDaoImplementation(DaoImpl.ARTICLE);
            dao.update(articleDBO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDriverAssistanceInfo() throws SQLException {
        DriverAssistanceDao dao = (DriverAssistanceDao) getDaoImplementation(DaoImpl
                .DRIVER_ASSISTANCE_INFO);
        dao.deleteBuilder().delete();
    }

    public void saveDriverAssistanceInfo(List<DriverAssistanceInfoDBO> list) throws SQLException {
        DriverAssistanceDao dao = (DriverAssistanceDao) getDaoImplementation(DaoImpl
                .DRIVER_ASSISTANCE_INFO);
        dao.saveOrUpdateList(list);
    }

    public List<DriverAssistanceInfoDBO> getDriverAssistanceInfo() {
        DriverAssistanceDao dao = (DriverAssistanceDao) getDaoImplementation(DaoImpl
                .DRIVER_ASSISTANCE_INFO);
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
