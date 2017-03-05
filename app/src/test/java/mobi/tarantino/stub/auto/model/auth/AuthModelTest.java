package mobi.tarantino.stub.auto.model.auth;

import org.junit.Test;

import mobi.tarantino.stub.auto.SchedulerProvider;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;

/**

 */
public class AuthModelTest {

    @Test
    public void smsTimerTest() {
        MobiAuthApi api = mock(MobiAuthApi.class);
        AuthModel model = new AuthModel(api);
        model.setSchedulerProvider(new SchedulerProvider() {
            @Override
            public Scheduler network() {
                return Schedulers.immediate();
            }

            @Override
            public Scheduler ui() {
                return Schedulers.immediate();
            }
        });
        Observable<Long> timer = model.getSmsTimer(5);
        timer.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(aLong);
            }
        });
    }

}