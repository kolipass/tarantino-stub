package mobi.tarantino.stub.auto.feature.dashboard.services;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**

 */
public class ServicesViewState implements RestorableViewState<ServicesView> {

    private static final String SCROLL_STATE = "scroll_state";

    private int position;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putInt(SCROLL_STATE, position);
    }

    @NonNull
    @Override
    public RestorableViewState<ServicesView> restoreInstanceState(@Nullable Bundle in) {
        if (in != null) {
            position = in.getInt(SCROLL_STATE);
        }
        return this;
    }

    @Override
    public void apply(@NonNull ServicesView view, boolean retained) {
        view.setScrollState(position);
    }

    public void setScrollState(int position) {
        this.position = position;
    }
}
