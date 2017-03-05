package mobi.tarantino.stub.auto.model.auth;


import android.support.annotation.NonNull;

import mobi.tarantino.stub.auto.model.auth.pojo.AuthCodeMobiApiAnswer;
import mobi.tarantino.stub.auto.model.auth.pojo.AuthTokenMobiApiAnswer;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Api only for getting token. Last docs located here: https://docs.google
 * .com/document/d/16v6JISwN3B4G9kTwMaJpIo5bOag14ahM5nMEeAAs2Tk
 */

public interface MobiAuthApi {
    @NonNull
    @POST("/authorize")
    @Headers("Cache-Control: no-cache")
    Observable<AuthCodeMobiApiAnswer> authorize(@Query("username") String username);

    @NonNull
    @FormUrlEncoded
    @POST("/token")
    @Headers("Cache-Control: no-cache")
    Observable<AuthTokenMobiApiAnswer> getToken(@Field("code") String codeFormAuth, @Field
            ("vcode") String smsCode);
}
