package mobi.tarantino.stub.auto.model.database.dbo;

import java.util.Collection;
import java.util.List;

import mobi.tarantino.stub.auto.model.additionalData.pojo.DriverAssistanceInfo;
import rx.Observable;
import rx.functions.Func1;

public class DriverAssistenceConverter {
    public static DriverAssistanceInfoDBO toDBO(DriverAssistanceInfo pojo) {
        DriverAssistanceInfoDBO dbo = new DriverAssistanceInfoDBO();
        return dbo
                .setId(pojo.getId())
                .setCreatedAt(pojo.getCreatedAt())
                .setPhoneNumber(pojo.getPhoneNumber())
                .setTitle(pojo.getTitle())
                ;
    }

    public static List<DriverAssistanceInfoDBO> toDBOCollection(Collection<DriverAssistanceInfo>
                                                                        pojos) {
        return Observable
                .from(pojos)
                .map(new Func1<DriverAssistanceInfo, DriverAssistanceInfoDBO>() {
                    @Override
                    public DriverAssistanceInfoDBO call(DriverAssistanceInfo pojo) {
                        return toDBO(pojo);
                    }
                })
                .toList()
                .toBlocking()
                .first();
    }

    public static DriverAssistanceInfo toPojo(DriverAssistanceInfoDBO dbo) {
        DriverAssistanceInfo pojo = new DriverAssistanceInfo();
        return pojo
                .setId(dbo.getId())
                .setCreatedAt(dbo.getCreatedAt())
                .setPhoneNumber(dbo.getPhoneNumber())
                .setTitle(dbo.getTitle())
                ;
    }

    public static List<DriverAssistanceInfo> toPojoCollection(final
                                                              Collection<DriverAssistanceInfoDBO>
                                                                      dbos) {
        return Observable
                .from(dbos)
                .map(new Func1<DriverAssistanceInfoDBO, DriverAssistanceInfo>() {
                    @Override
                    public DriverAssistanceInfo call(DriverAssistanceInfoDBO dbo) {
                        return toPojo(dbo);
                    }
                })
                .toList()
                .toBlocking()
                .first();
    }
}
