package mobi.tarantino.stub.auto.model.additionalData;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.IPreferencesManager;
import mobi.tarantino.stub.auto.R;
import mobi.tarantino.stub.auto.eventbus.RefreshNotificationEvent;
import mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard.NotificationDTO;
import mobi.tarantino.stub.auto.model.AbstractModel;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.model.additionalData.pojo.DriverAssistanceInfo;
import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;
import mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestion;
import mobi.tarantino.stub.auto.model.database.dbo.AnswerQuestionConverter;
import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;
import mobi.tarantino.stub.auto.model.database.dbo.DriverAssistanceInfoDBO;
import mobi.tarantino.stub.auto.model.location.LocationModel;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static mobi.tarantino.stub.auto.model.database.dbo.DriverAssistenceConverter.toDBOCollection;
import static mobi.tarantino.stub.auto.model.database.dbo.DriverAssistenceConverter
        .toPojoCollection;


public class MobiAdditionalModel extends AbstractModel {

    private MobiAdditionalDataApi api;
    private MobiAdditionalDatabaseModel databaseModel;
    private IPreferencesManager preferencesManager;
    private LocationModel locationModel;
    private Context context;

    @Inject
    public MobiAdditionalModel(MobiAdditionalDataApi api) {
        this.api = api;
    }

    @Inject
    public void setDatabaseModel(MobiAdditionalDatabaseModel model) {
        databaseModel = model;
    }

    @Inject
    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    @Inject
    public void setPreferencesManager(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    @Inject
    public void setContext(Context context) {
        this.context = context;
    }

    public Observable<List<DriverAssistanceInfo>> getDriverAssistanceInfo() {
        Observable<List<DriverAssistanceInfo>> apiObservable = getCity()
                .flatMap(new Func1<String, Observable<List<DriverAssistanceInfo>>>() {
                    @Override
                    public Observable<List<DriverAssistanceInfo>> call(String cityName) {
                        return api.getDriverAssistanceInfo(cityName);
                    }
                })
                .doOnNext(savePhonesToDb());

        Observable<List<DriverAssistanceInfo>> dboObservable = Observable
                .just(databaseModel.getDriverAssistanceInfo())
                .map(new Func1<List<DriverAssistanceInfoDBO>, List<DriverAssistanceInfo>>() {
                    @Override
                    public List<DriverAssistanceInfo> call(List<DriverAssistanceInfoDBO> list) {
                        return toPojoCollection(list);
                    }
                });


        Observable<List<DriverAssistanceInfo>> listObservable = Observable
                .mergeDelayError(dboObservable,
                        apiObservable
                )
                .doOnNext(new Action1<List<DriverAssistanceInfo>>() {
                    @Override
                    public void call(List<DriverAssistanceInfo> driverAssistanceInfos) {
                        addDefaultDriverAssistanceInfo(driverAssistanceInfos);
                    }
                });


        return wrapDefaultObservableObject(listObservable);
    }

    @NonNull
    private Observable<String> getCity() {
        return locationModel
                .getCurrentAddressLocation()
                .map(new Func1<Address, String>() {
                    @Override
                    public String call(Address address) {
                        return address.getLocality();
                    }
                });
    }

    public Observable<String> getCityInBg() {
        return wrapDefaultObservableObject(getCity());
    }

    @NonNull
    private Action1<List<DriverAssistanceInfo>> savePhonesToDb() {
        return new Action1<List<DriverAssistanceInfo>>() {
            @Override
            public void call(List<DriverAssistanceInfo> driverAssistanceInfos) {
                try {
                    databaseModel.clearDriverAssistanceInfo();
                    databaseModel.saveDriverAssistanceInfo(toDBOCollection(driverAssistanceInfos));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private void addDefaultDriverAssistanceInfo(List<DriverAssistanceInfo> driverAssistanceInfos) {
        DriverAssistanceInfo defaultInfo = new DriverAssistanceInfo();
        defaultInfo.setPhoneNumber(context.getString(R.string.common_emergency_phone));
        defaultInfo.setTitle(context.getString(R.string.emergency));
        driverAssistanceInfos.add(0, defaultInfo);
    }

    public Observable<List<Article>> getArticles(long timeInSec) {
        return wrapList(api.getArticles(timeInSec));
    }

    public Observable<List<Article>> getArticlesDecSort(long timeInSec) {
        Observable<List<Article>> articles = api
                .getArticles(timeInSec)
                .doOnNext(new SortArticles());

        return wrapList(articles);
    }


    public Observable<List<Question>> getQuestions() {
        if (random.nextBoolean()) {
            return wrapList(api.getQuestions(0))
                    .map(AnswerQuestionConverter.getAnswerQuestionConverter())
                    .map(new Func1<List<AnswerQuestion>, List<AnswerQuestion>>() {
                        @Override
                        public List<AnswerQuestion> call(List<AnswerQuestion> list) {
                            SparseArray<AnswerQuestion> cash = databaseModel
                                    .getAnsweredQuestionMap();

                            List<AnswerQuestion> freshList = new ArrayList<>();

                            for (AnswerQuestion fromServer : list) {
                                if (cash.get(fromServer.getId()) == null) {
                                    freshList.add(fromServer);
                                }
                            }


                            databaseModel.update(freshList);
                            return freshList;
                        }
                    })
                    .map(AnswerQuestionConverter.getQuestionConverter());
        } else {
            List<Question> value = new ArrayList<Question>() {
                {
                    add(new Question(100, "Свободу попугаям", "3434", ""));
                }
            };
            return Observable.just(value);
        }
    }

    public Observable<FuelPrices> getFuelPrices(String location) {
        if (random.nextBoolean()) {
            return wrap(api.getFuelPrices(location));
        } else {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(FuelPrices.class, new FuelPrices());
            Gson gson = builder.create();
            FuelPrices fuelPrices = gson
                    .fromJson("{\"ai92\":[[\"04.03.17\",\"35,71\",\"0,02\"],[\"03.03.17\"," +
                            "\"35,69\",\"0,00\"],[\"02.03.17\",\"35,69\",\"0,02\"]],\"ai95\":" +
                            "[[\"04.03.17\",\"38,87\",\"0,01\"],[\"03.03.17\",\"38,86\",\"-0,01" +
                            "\"],[\"02.03.17\",\"38,87\",\"0,01\"]],\"ai98\":[[\"04.03.17\",\"43" +
                            ",13\",\"0,01\"],[\"03.03.17\",\"43,12\",\"-0,04\"],[\"02.03.17\",\"" +
                            "43,16\",\"0,00\"]],\"diesel\":[[\"04.03.17\",\"37,23\",\"-0,01\"]" +
                            ",[\"03.03.17\",\"37,24\",\"0,00\"],[\"02.03.17\",\"37,24\",\"0,00" +
                            "\"]]}", FuelPrices.class);
            return Observable.just(fuelPrices);
        }
    }

    public Observable<Response<ResponseBody>> likeQuestion(final Question question) {
        return wrapDefaultResponse(api.likeQuestion(question.getYesUrl()))
                .doOnNext(new Action1<Response<ResponseBody>>() {
                    @Override
                    public void call(Response<ResponseBody> response) {
                        databaseModel.setAnswerTime(question.getId());
                    }
                });
    }

    public Observable<Response<ResponseBody>> dislikeQuestion(final Question question) {
        return wrapDefaultResponse(api.dislikeQuestion(question.getNoUrl()))
                .doOnNext(new Action1<Response<ResponseBody>>() {
                    @Override
                    public void call(Response<ResponseBody> response) {
                        databaseModel.setAnswerTime(question.getId());
                    }
                });
    }

    public void saveEvent(ArticleDBO articleDBO) {
        databaseModel.saveEvent(articleDBO);
    }

    public void saveLaw(ArticleDBO articleDBO) {
        databaseModel.saveLaw(articleDBO);
    }


    public Observable<NotificationDTO> getNotificationDTO(boolean refresh) {


        Observable<List<ArticleDBO>> eventListObs = Observable.combineLatest(
//                api.getPartnerActions(0)
                getPartnerActions()
                        .doOnNext(new SortArticles()),
                Observable.just(databaseModel.getAllPartnerActionsMap()),
                new MergeDboArticles(ArticleDBO.TYPE_PARTNER_ACTIONS)
        )
                .doOnNext(databaseModel.updateArticles())
                .doOnNext(new FillTitle("Акция"));

        Observable<List<ArticleDBO>> lawListObs = Observable.combineLatest(
//                api.getArticles(0)
                getArticles()
                        .doOnNext(new SortArticles()),
                Observable.just(databaseModel.getAllLawsMap()),
                new MergeDboArticles(ArticleDBO.TYPE_LAW)
        )
                .doOnNext(databaseModel.updateArticles())
                .doOnNext(new FillTitle("Новики"));

        return wrapDefaultObservableObject(Observable.zip(eventListObs, lawListObs, new
                Func2<List<ArticleDBO>, List<ArticleDBO>, NotificationDTO>() {
                    @Override
                    public NotificationDTO call(List<ArticleDBO> events, List<ArticleDBO> laws) {
                        return new NotificationDTO(events, laws, preferencesManager
                                .getNewFinesCount());
                    }
                }));

    }

    Observable<List<Article>> getPartnerActions() {
        List<Article> articles = new ArrayList<>();
        if (random.nextBoolean()) {
            articles.add(new Article()
                    .setTitle("Все шапки по 50!")
                    .setCreatedAt(System.currentTimeMillis())
            );
        }

        return Observable.just(articles);
    }

    Observable<List<Article>> getArticles() {
        List<Article> articles = new ArrayList<>();
        if (random.nextBoolean()) {
            articles.add(new Article()
                    .setTitle("Шапки из меха")
                    .setCreatedAt(System.currentTimeMillis())
            );
        }

        return Observable.just(articles);
    }

    public void markViewedArticle(ArticleDBO articleDBO) {
        databaseModel.markViewedArticle(articleDBO);

        EventBus.getDefault().post(new RefreshNotificationEvent(RefreshNotificationEvent.Type
                .UPDATE));
    }

    class SortArticles implements Action1<List<Article>> {
        @Override
        public void call(List<Article> articles) {
            Collections.sort(articles);
        }
    }

    private class MergeDboArticles implements Func2<List<Article>, Map<Integer, ArticleDBO>,
            List<ArticleDBO>> {
        private String typeEvent;
        private boolean skipViewed;

        public MergeDboArticles(String typeEvent) {
            this.typeEvent = typeEvent;
        }

        public MergeDboArticles(String typeEvent, boolean skipViewed) {
            this.typeEvent = typeEvent;
            this.skipViewed = skipViewed;
        }

        @Override
        public List<ArticleDBO> call(List<Article> articles, Map<Integer, ArticleDBO>
                integerArticleDBOMap) {
            List<ArticleDBO> result = new ArrayList<>();
            for (Article article : articles) {

                ArticleDBO articleDBO = integerArticleDBOMap.get(article.getId());
                articleDBO = ArticleDBO.createOrUpdate(articleDBO, article);
                if (!(articleDBO.isViewed() && skipViewed)) {
                    articleDBO.setType(typeEvent);

                    result.add(articleDBO);
                }
            }
            return result;
        }
    }

    private class FillTitle implements Action1<List<ArticleDBO>> {
        private String title;

        FillTitle(String title) {
            this.title = title;
        }

        @Override
        public void call(List<ArticleDBO> articles) {
            for (ArticleDBO article : articles) {
                if (TextUtils.isEmpty(article.getTitle())) {
                    article.setTitle(title + " #" + article.getId());
                }
            }
        }
    }
}
