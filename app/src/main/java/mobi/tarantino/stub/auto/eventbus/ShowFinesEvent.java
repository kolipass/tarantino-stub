package mobi.tarantino.stub.auto.eventbus;

public class ShowFinesEvent {

    private int tab;

    public ShowFinesEvent(int tab) {
        this.tab = tab;
    }

    public int getTab() {
        return tab;
    }
}
