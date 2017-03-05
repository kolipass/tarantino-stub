package mobi.tarantino.stub.auto.mvp.viewState;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

import mobi.tarantino.stub.auto.mvp.view.CrudView;

import static mobi.tarantino.stub.auto.Consts.Key.EXCEPTION;
import static mobi.tarantino.stub.auto.Consts.Key.POOL_TO_REFRESH;
import static mobi.tarantino.stub.auto.Consts.Key.VIEW_STATE;

/**

 */

public class CrudViewState<V extends CrudView> implements RestorableViewState<V> {

    private CrudViewState.EditorState state = EditorState.DEFAULT;
    private Throwable exception;
    private boolean poolToRefresh;

    @Override
    public void apply(V view, boolean retained) {
        switch (state) {
            case CREATE_PROGRESS:
                view.onCreateProgress();
                break;

            case CREATE_SUCCESS:
                view.onCreateSuccess();
                break;

            case CREATE_FAILED:
                view.onCreateFailed(exception);
                break;

            case UPDATE_PROGRESS:
                view.onUpdateProgress();
                break;

            case UPDATE_SUCCESS:
                view.onUpdateSuccess();
                break;

            case UPDATE_FAILED:
                view.onUpdateFailed(exception);
                break;

            case REMOVE_PROGRESS:
                view.onRemoveProgress();
                break;
            case REMOVE_SUCCESS:
                view.onRemoveSuccess();
                break;
            case REMOVE_FAILED:
                view.onRemoveFailed(exception);
                break;
        }
    }

    public void setCreateSuccessState() {
        state = CrudViewState.EditorState.CREATE_SUCCESS;
    }

    public void setCreateProgressState() {
        state = CrudViewState.EditorState.CREATE_PROGRESS;
    }

    public void setCreateFailedState(Throwable e, boolean poolToRefresh) {
        state = CrudViewState.EditorState.CREATE_FAILED;
    }

    public void setUpdateProgressState() {
        state = CrudViewState.EditorState.UPDATE_PROGRESS;
    }

    public void setUpdateSuccessState() {
        state = CrudViewState.EditorState.UPDATE_SUCCESS;
    }

    public void setUpdateFailedState(Throwable e, boolean poolToRefresh) {
        state = CrudViewState.EditorState.UPDATE_FAILED;
    }

    public void setRemoveFailedState(Throwable e, boolean poolToRefresh) {
        state = CrudViewState.EditorState.REMOVE_FAILED;
    }

    public void setRemoveProgressState() {
        state = CrudViewState.EditorState.REMOVE_PROGRESS;
    }

    public void setRemoveSuccessState() {
        state = CrudViewState.EditorState.REMOVE_SUCCESS;
    }


    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putSerializable(VIEW_STATE, state);
        out.putSerializable(EXCEPTION, exception);
        out.putBoolean(POOL_TO_REFRESH, poolToRefresh);
    }

    @Override
    public RestorableViewState<V> restoreInstanceState(Bundle in) {
        if (in != null) {
            state = (EditorState) in.getSerializable(VIEW_STATE);
            exception = (Throwable) in.getSerializable(EXCEPTION);
            poolToRefresh = (boolean) in.getSerializable(POOL_TO_REFRESH);

        }
        return this;
    }

    public enum EditorState {
        CREATE_PROGRESS, CREATE_SUCCESS, CREATE_FAILED, UPDATE_PROGRESS, UPDATE_SUCCESS,
        UPDATE_FAILED,
        REMOVE_PROGRESS, REMOVE_SUCCESS, REMOVE_FAILED, DEFAULT

    }
}
