package mobi.tarantino.stub.auto.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.SchedulerProvider;
import mobi.tarantino.stub.auto.model.database.dbo.DBO;
import mobi.tarantino.stub.auto.model.rxHelper.ParseErrorResponseFunc;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

/**

 */

public class AbstractModel {
    protected SchedulerProvider schedulerProvider;
    protected Gson gson;

    @NonNull
    protected Random random = new Random();

    @Inject
    public void setSchedulerProvider(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    @Inject
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    protected <N extends ApiResponse> Observable<N> wrap(@NonNull Observable<N> observable) {
        return observable
                .onErrorResumeNext(new ParseErrorResponseFunc<N>().setGson(gson))
                .subscribeOn(schedulerProvider.network())
                .observeOn(schedulerProvider.ui());
    }

    protected <N extends List<? extends ApiResponse>> Observable<N> wrapList(@NonNull Observable<N>
                                                                                     observable) {
        return observable
                .subscribeOn(schedulerProvider.network())
                .observeOn(schedulerProvider.ui());
    }

    protected <N extends Response<ResponseBody>> Observable<N> wrapDefaultResponse(
            @NonNull Observable<N> observable) {
        return observable
                .subscribeOn(schedulerProvider.network())
                .observeOn(schedulerProvider.ui());
    }

    protected <N extends DBO> Observable<N> wrapDBO(N dbo) {
        return Observable.just(dbo)
                .subscribeOn(schedulerProvider.network())
                .observeOn(schedulerProvider.ui());

    }

    protected <N extends List<? extends DBO>> Observable<N> wrapListDBO(N dboList) {
        return Observable.just(dboList)
                .subscribeOn(schedulerProvider.network())
                .observeOn(schedulerProvider.ui());

    }

    protected <N> Observable<N> wrapDefaultObservableObject(@NonNull Observable<N>
                                                                    observable) {
        return observable
                .subscribeOn(schedulerProvider.network())
                .observeOn(schedulerProvider.ui());
    }

}
