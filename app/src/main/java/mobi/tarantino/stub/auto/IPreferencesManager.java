package mobi.tarantino.stub.auto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public interface IPreferencesManager {
    @Nullable
    String getToken();

    void setToken(@NonNull String token);

    @NonNull
    @NotNull
    String getPhone();

    void setPhone(@NonNull String phone);

    boolean isDemoTourFinished();

    void demoTourFinish(boolean isFinish);

    void clearAll();

    void setFinePayCount(String count);

    void setFineNotPayCount(String count);

    @Nullable
    String getGaClientId();

    void setGaClientId(String id);

    @Nullable
    String getFinesPayCount();

    @Nullable
    String getFinesNotPayCount();

    @Nullable
    String getDriversCount();

    void setDriversCount(String count);

    @Nullable
    String getCarsCount();

    void setCarsCount(String carsCount);

    int getNewFinesCount();

    void setNewFinesCount(int count);

    @Nullable
    String getGcmToken();

    void setGcmToken(String token);

    boolean isRegisterGcmOnServer();

    void setRegisterGcmOnServer(boolean isRegistered);

    void clearFines();
}
