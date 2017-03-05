package mobi.tarantino.stub.auto.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**

 */

public interface CrudView extends MvpView {
    void onCreateSuccess();

    void onCreateFailed(Throwable e);

    void onCreateProgress();

    void onUpdateSuccess();

    void onUpdateFailed(Throwable e);

    void onUpdateProgress();

    void onRemoveSuccess();

    void onRemoveFailed(Throwable e);

    void onRemoveProgress();
}
