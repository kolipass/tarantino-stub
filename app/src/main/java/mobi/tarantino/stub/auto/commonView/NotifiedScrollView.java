package mobi.tarantino.stub.auto.commonView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * http://stackoverflow.com/a/21894753
 */

public class NotifiedScrollView extends ScrollView {
    @NonNull
    protected Set<Integer> _shownViewsIndices = new HashSet<>();
    protected OnChildViewVisibilityChangedListener listener;

    /**
     * Minimal percent of child visibility to notification {@link #listener}
     * 0 - not be used
     */
    private int percentOfVisibility = 0;

    public NotifiedScrollView(final Context context) {
        super(context);
    }

    public NotifiedScrollView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifiedScrollView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnChildViewVisibilityChangedListener(
            final OnChildViewVisibilityChangedListener
                    onChildViewVisibilityChangedListener) {
        listener = onChildViewVisibilityChangedListener;
    }

    public int getPercentOfVisibility() {
        return percentOfVisibility;
    }

    /**
     * Minimal percent of child visibility to notification {@link #listener}
     * Not be used if {@link #listener}=0
     * or if {@link #listener} > than maxinal visible size of children (children larger than parent)
     */
    @NonNull
    public NotifiedScrollView setPercentOfVisibility(int percentOfVisibility) {
        this.percentOfVisibility = percentOfVisibility;
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            checkViewsVisibility(left, top);
        }
    }

    private void checkViewsVisibility(final int left, final int parentTop) {
        final int parentBottom = parentTop + getHeight();

        final ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        final int childCount = viewGroup.getChildCount();
        if (childCount == 0) {
            return;
        }

        // check previously shown views
        for (final Iterator<Integer> iterator = _shownViewsIndices.iterator(); iterator.hasNext()
                ; ) {
            final Integer currentViewIndex = iterator.next();
            View v = viewGroup.getChildAt(currentViewIndex);

            if (!isViewInVisibleArea(parentTop, parentBottom, v)) {
                notifyListener(v, currentViewIndex, false);
                iterator.remove();
            }
        }

        for (int i = 0; i < childCount; ++i) {
            View v = viewGroup.getChildAt(i);
            if (isViewInVisibleButNonNotified(parentTop, parentBottom, v, i)) {
                _shownViewsIndices.add(i);
                notifyListener(v, i, true);
            }
        }
    }

    private void notifyListener(@NonNull View view, int index, boolean becameVisible) {
        if (listener != null && view.getVisibility() != GONE) {
            listener.onChildViewVisibilityChanged(index, view, becameVisible);
        }
    }

    private boolean isViewInVisibleButNonNotified(int parentTop, int parentBottom, @NonNull View
            v, int i) {
        return isViewInVisibleArea(parentTop, parentBottom, v) && !_shownViewsIndices.contains(i);
    }

    protected boolean isViewInVisibleArea(int parentTop, int parentBottom, @NonNull View view) {
        int childTop = view.getTop();
        int childBottom = view.getBottom();

        return isViewInVisibleArea(parentTop, parentBottom, childTop, childBottom);
    }

    protected boolean isViewInVisibleArea(int parentTop, int parentBottom, int childTop, int
            childBottom) {
        int childLength = childBottom - childTop;
        int parentLength = parentBottom - parentTop;
        if (childLength > 0 && parentLength > 0) {
            if (usePercentOfVisibility(childLength, parentLength)) {
                int visibleLength = getInVisibleArea(parentTop, parentBottom, childTop,
                        childBottom);

                return (float) visibleLength / childLength * 100 >= percentOfVisibility;
            } else {
                return childTop <= parentBottom && childBottom >= parentTop;
            }
        } else {
            return false;
        }
    }

    private boolean usePercentOfVisibility(int childLength, int parentLength) {
        float ratio = percentOfVisibility / 100f;

        int lenghtDiff = parentLength - childLength;
        float maxVisibleRatio = lenghtDiff > 0 ? 1 : (float) parentLength / childLength;

        return percentOfVisibility != 0 && ratio <= maxVisibleRatio;
    }

    protected int getInVisibleArea(int parentTop, int parentBottom, int childTop, int childBottom) {
        int childLength = childBottom - childTop;
        int parentLength = parentBottom - parentTop;

        if (childBottom <= parentBottom) {
            if (childTop > parentTop) {
                return childLength;
            } else if (parentTop <= childBottom) {
                return childBottom - parentTop;
            } else {
                return 0;
            }

        } else {
            if (childTop < parentTop) {
                return parentLength;
            } else if (parentBottom >= childTop) {
                return parentBottom - childTop;
            } else {
                return 0;
            }
        }

    }

    @Override
    protected void onScrollChanged(final int currentHorizontalScroll, final int
            currentVerticalScroll,
                                   final int prevHorizontalScroll, final int prevVerticalScroll) {
        super.onScrollChanged(currentHorizontalScroll, currentVerticalScroll,
                prevHorizontalScroll, prevVerticalScroll);

        checkViewsVisibility(currentHorizontalScroll, currentVerticalScroll);
    }

    public interface OnChildViewVisibilityChangedListener {
        void onChildViewVisibilityChanged(int index, View v, boolean becameVisible);
    }
}