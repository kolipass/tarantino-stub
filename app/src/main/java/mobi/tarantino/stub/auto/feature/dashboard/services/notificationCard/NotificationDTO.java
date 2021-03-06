package mobi.tarantino.stub.auto.feature.dashboard.services.notificationCard;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import mobi.tarantino.stub.auto.model.database.dbo.ArticleDBO;

public class NotificationDTO implements Parcelable {

    public static final Creator<NotificationDTO> CREATOR = new Creator<NotificationDTO>() {
        @Override
        public NotificationDTO createFromParcel(Parcel in) {
            return new NotificationDTO(in);
        }

        @Override
        public NotificationDTO[] newArray(int size) {
            return new NotificationDTO[size];
        }
    };
    private List<ArticleDBO> allEvents;
    private List<ArticleDBO> allLaws;
    private int finesCount = 0;

    public NotificationDTO(List<ArticleDBO> allEvents, List<ArticleDBO> allLaws, int finesCount) {
        this.allEvents = allEvents;
        this.allLaws = allLaws;
        this.finesCount = finesCount;
    }

    protected NotificationDTO(Parcel in) {
        allEvents = in.createTypedArrayList(ArticleDBO.CREATOR);
        allLaws = in.createTypedArrayList(ArticleDBO.CREATOR);
        finesCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(allEvents);
        dest.writeTypedList(allLaws);
        dest.writeInt(finesCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<ArticleDBO> getAllEvents() {
        return allEvents;
    }

    public List<ArticleDBO> getAllLaws() {
        return allLaws;
    }

    public int getFinesCount() {
        return finesCount;
    }
}
