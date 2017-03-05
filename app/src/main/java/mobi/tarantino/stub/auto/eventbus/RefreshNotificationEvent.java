package mobi.tarantino.stub.auto.eventbus;

public class RefreshNotificationEvent {
    private Type type;

    public RefreshNotificationEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ADD,
        DELETE,
        UPDATE
    }
}
