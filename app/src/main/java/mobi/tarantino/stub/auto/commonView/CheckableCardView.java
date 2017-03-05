package mobi.tarantino.stub.auto.commonView;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.Checkable;

import static mobi.tarantino.stub.auto.helper.ViewHelper.checkAllViewGroup;

public class CheckableCardView extends CardView implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean mIsChecked;

    public CheckableCardView(Context context) {
        super(context);
    }

    public CheckableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void setChecked(boolean isChecked) {
        boolean wasChecked = isChecked();
        mIsChecked = isChecked;

        if (wasChecked ^ mIsChecked) {
            checkAllViewGroup(this, isChecked);
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }

}
