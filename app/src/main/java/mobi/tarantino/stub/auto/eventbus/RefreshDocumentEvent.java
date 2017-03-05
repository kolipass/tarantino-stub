package mobi.tarantino.stub.auto.eventbus;


import android.support.annotation.NonNull;

public class RefreshDocumentEvent {

    private boolean finesCardReceived = false;
    private boolean finesListReceived = false;
    private boolean profileReceived = false;

    public boolean isFinesCardReceived() {
        return finesCardReceived;
    }

    @NonNull
    public RefreshDocumentEvent setFinesCardReceived(boolean finesCardReceived) {
        this.finesCardReceived = finesCardReceived;
        return this;
    }

    public boolean isProfileReceived() {
        return profileReceived;
    }

    @NonNull
    public RefreshDocumentEvent setProfileReceived(boolean profileReceived) {
        this.profileReceived = profileReceived;
        return this;
    }

    public boolean isFinesListReceived() {
        return finesListReceived;
    }

    @NonNull
    public RefreshDocumentEvent setFinesListReceived(boolean finesListReceived) {
        this.finesListReceived = finesListReceived;
        return this;
    }

    public boolean isAllReceived() {
        return finesCardReceived && finesListReceived && profileReceived;
    }
}
