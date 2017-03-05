package mobi.tarantino.stub.auto.model.database.dbo;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.pojo.DriverAssistanceInfo;
import rx.Observable;
import rx.functions.Func1;

public class DriverAssistenceConverter {
    @NonNull
    public static DriverAssistanceInfoDBO toDBO(@NonNull DriverAssistanceInfo pojo) {
        DriverAssistanceInfoDBO dbo = new DriverAssistanceInfoDBO();
        return dbo
                .setId(pojo.getId())
                .setCreatedAt(pojo.getCreatedAt())
                .setPhoneNumber(pojo.getPhoneNumber())
                .setTitle(pojo.getTitle())
                ;
    }

    public static List<DriverAssistanceInfoDBO> toDBOCollection(
            @NonNull Collection<DriverAssistanceInfo>
                    pojos) {
        return Observable
                .from(pojos)
                .map(new Func1<DriverAssistanceInfo, DriverAssistanceInfoDBO>() {
                    @NonNull
                    @Override
                    public DriverAssistanceInfoDBO call(@NonNull DriverAssistanceInfo pojo) {
                        return toDBO(pojo);
                    }
                })
                .toList()
                .toBlocking()
                .first();
    }

    public static DriverAssistanceInfo toPojo(@NonNull DriverAssistanceInfoDBO dbo) {
        DriverAssistanceInfo pojo = new DriverAssistanceInfo();
        return pojo
                .setId(dbo.getId())
                .setCreatedAt(dbo.getCreatedAt())
                .setPhoneNumber(dbo.getPhoneNumber())
                .setTitle(dbo.getTitle())
                ;
    }

    public static List<DriverAssistanceInfo> toPojoCollection(@NonNull final
                                                              Collection<DriverAssistanceInfoDBO>
                                                                      dbos) {
        return Observable
                .from(dbos)
                .map(new Func1<DriverAssistanceInfoDBO, DriverAssistanceInfo>() {
                    @Override
                    public DriverAssistanceInfo call(@NonNull DriverAssistanceInfoDBO dbo) {
                        return toPojo(dbo);
                    }
                })
                .toList()
                .toBlocking()
                .first();
    }
}
