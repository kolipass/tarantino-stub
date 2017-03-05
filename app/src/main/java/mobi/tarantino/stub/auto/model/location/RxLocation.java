package mobi.tarantino.stub.auto.model.location;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import mobi.tarantino.stub.auto.Logger;
import rx.Observable;
import rx.Subscriber;

/**

 */

public class RxLocation implements Observable.OnSubscribe<Address> {

    private GoogleApiClient apiClient;
    private Context context;

    public RxLocation(@NonNull Context context) {
        this.context = context;
        apiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION})
    @Override
    public void call(@NonNull Subscriber<? super Address> subscriber) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS) {
            try {
                if (apiClient.blockingConnect(5, TimeUnit.SECONDS).isSuccess()) {
                    Location location = LocationServices.FusedLocationApi.getLastLocation
                            (apiClient);
                    if (location == null) {
                        defaultResult(subscriber);
                        return;
                    }

                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    if (addresses != null && addresses.size() != 0) {
                        subscriber.onNext(addresses.get(0));
                        subscriber.onCompleted();
                    } else {
                        defaultResult(subscriber);
                    }
                } else {
                    defaultResult(subscriber);
                }
            } catch (SecurityException ex) {
                Logger.DEFAULT.log(ex.getMessage());
                subscriber.onError(ex);
            } catch (IOException ex) {
                Logger.DEFAULT.log(ex.getMessage());
                subscriber.onError(new UnknownHostException());
            } finally {
                disconnect();
            }
        } else {
            defaultResult(subscriber);
        }
    }

    private void defaultResult(@NonNull Subscriber<? super Address> subscriber) {
        Address defaultAddress = new Address(Locale.getDefault());
        subscriber.onNext(defaultAddress);
        subscriber.onCompleted();
    }

    private void disconnect() {
        if (apiClient != null) {
            apiClient.disconnect();
        }
    }
}
