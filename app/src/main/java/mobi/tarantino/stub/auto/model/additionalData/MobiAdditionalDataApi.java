package mobi.tarantino.stub.auto.model.additionalData;


import android.support.annotation.NonNull;

import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.pojo.Article;
import mobi.tarantino.stub.auto.model.additionalData.pojo.DriverAssistanceInfo;
import mobi.tarantino.stub.auto.model.additionalData.pojo.FuelPrices;
import mobi.tarantino.stub.auto.model.additionalData.pojo.Question;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Api for traffic fines. Last docs located here: https://docs.google
 * .com/spreadsheets/d/1ER0LhEUG2g_ddOrv2uiQfRm36gt_nwpilUzaemZsOKI/edit#gid=0
 */

public interface MobiAdditionalDataApi {
    String API_V1 = "/api/v1/";

    /**
     * @param timeInSec last_query_time - время в секундах от 1970, если его нет, то вернет все
     *                  статьи. 0  - все
     * @return list of articles
     */
    @NonNull
    @GET(API_V1 + "articles")
    Observable<List<Article>> getArticles(@Query("last_query_time") long timeInSec);

    /**
     * @param city - наименование города на русском (пример: Москва)
     * @return [{"number":"+7 (47461) 20256"}]
     */
    @NonNull
    @GET(API_V1 + "phones")
    Observable<List<DriverAssistanceInfo>> getDriverAssistanceInfo(@Query("city") String city);

    @NonNull
    @GET(API_V1 + "partner_actions")
    Observable<List<Article>> getPartnerActions(@Query("last_query_time") long timeInSec);

    @NonNull
    @GET(API_V1 + "fuel_prices")
    Observable<FuelPrices> getFuelPrices(@Query("city") String city);

//    region Questions

    @NonNull
    @GET(API_V1 + "questions")
    Observable<List<Question>> getQuestions(@Query("last_query_time") long timeInSec);

    @NonNull
    @GET
    Observable<Response<ResponseBody>> likeQuestion(@Url String yesUrl);

    @NonNull
    @GET
    Observable<Response<ResponseBody>> dislikeQuestion(@Url String noUrl);
//endregion
}
