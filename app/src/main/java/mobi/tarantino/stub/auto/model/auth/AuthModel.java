package mobi.tarantino.stub.auto.model.auth;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.model.AbstractModel;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthCodeMobiApiAnswer;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthTokenMobiApiAnswer;
import rx.Observable;


public class AuthModel extends AbstractModel {
    protected MobiAuthApi api;


    @Inject
    public AuthModel(MobiAuthApi api) {
        this.api = api;
    }


    public Observable<AuthCodeMobiApiAnswer> sendPhone(final String phone) {
        Observable<AuthCodeMobiApiAnswer> observable;
        if (random.nextBoolean()) {
            observable = wrap(api.authorize(phone));
        } else {
            observable = Observable.just(new AuthCodeMobiApiAnswer().setCode("555"));
        }
        return observable;
    }

    public Observable<AuthTokenMobiApiAnswer> sendCode(String codeFormAuth, String smsCode) {
        if (random.nextBoolean()) {
            return wrap(api.getToken(codeFormAuth, smsCode));
        } else {
            return Observable.just(new AuthTokenMobiApiAnswer()
                    .setAccessToken("555")
                    .setPhone("4444444"));
        }


    }

    public Observable<Long> getSmsTimer(final long timerSeconds) {
        return wrapDefaultObservableObject(Observable.interval(1, TimeUnit.SECONDS,
                schedulerProvider.network())
                .take((int) timerSeconds));
    }

}
