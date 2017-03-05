package mobi.tarantino.stub.auto.eventbus;


public class RefreshDocumentEvent {

    private boolean finesCardReceived = false;
    private boolean finesListReceived = false;
    private boolean profileReceived = false;

    public boolean isFinesCardReceived() {
        return finesCardReceived;
    }

    public RefreshDocumentEvent setFinesCardReceived(boolean finesCardReceived) {
        this.finesCardReceived = finesCardReceived;
        return this;
    }

    public boolean isProfileReceived() {
        return profileReceived;
    }

    public RefreshDocumentEvent setProfileReceived(boolean profileReceived) {
        this.profileReceived = profileReceived;
        return this;
    }

    public boolean isFinesListReceived() {
        return finesListReceived;
    }

    public RefreshDocumentEvent setFinesListReceived(boolean finesListReceived) {
        this.finesListReceived = finesListReceived;
        return this;
    }

    public boolean isAllReceived() {
        return finesCardReceived && finesListReceived && profileReceived;
    }
}
