package mobi.tarantino.stub.auto.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**

 */
public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity<V, P> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void injectDependencies();
}
