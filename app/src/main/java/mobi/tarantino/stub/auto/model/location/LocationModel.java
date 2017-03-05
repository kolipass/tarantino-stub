package mobi.tarantino.stub.auto.model.location;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;

import rx.Observable;

/**

 */

public class LocationModel {

    private Context context;
    private Observable<Address> addressObservable;

    public LocationModel(Context context) {
        this.context = context;
    }

    @NonNull
    public Observable<Address> getCurrentAddressLocation() {
        if (addressObservable == null) {
            addressObservable = Observable.create(new RxLocation(context));
        }
        return addressObservable.cache();
    }

}
