package mobi.tarantino.stub.auto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface IPreferencesManager {
    @Nullable
    String getToken();

    void setToken(@NonNull String token);

    @Nullable
    String getPhone();

    void setPhone(@NonNull String phone);

    boolean isDemoTourFinished();

    void demoTourFinish(boolean isFinish);

    void clearAll();

    void setFinePayCount(String count);

    void setFineNotPayCount(String count);

    String getGaClientId();

    void setGaClientId(String id);

    String getFinesPayCount();

    String getFinesNotPayCount();

    String getDriversCount();

    void setDriversCount(String count);

    String getCarsCount();

    void setCarsCount(String carsCount);

    int getNewFinesCount();

    void setNewFinesCount(int count);

    String getGcmToken();

    void setGcmToken(String token);

    boolean isRegisterGcmOnServer();

    void setRegisterGcmOnServer(boolean isRegistered);

    void clearFines();
}
