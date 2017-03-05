package mobi.tarantino.stub.auto.commonView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.test.mock.MockContext;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotifiedScrollViewTest {
    @SuppressLint("WrongCall")
    @Test
    public void onLayout() throws Exception {
        NotifiedScrollView scrollView = spy(new NotifiedScrollView(new MockContext()));

        NotifiedScrollView.OnChildViewVisibilityChangedListener listener = mock
                (NotifiedScrollView.OnChildViewVisibilityChangedListener.class);
        scrollView.setOnChildViewVisibilityChangedListener(listener);

        ViewGroup container = new StubViewGroup(new MockContext());
        container.addView(getMockView(0, 50));
        when(scrollView.getChildAt(anyInt())).thenReturn(container);

        int height = 100;
        when(scrollView.getHeight()).thenReturn(height);

        scrollView.onLayout(true, 0, 0, 0, height);
        verify(listener, only()).onChildViewVisibilityChanged(eq(0), (View) any(), eq(true));


        reset(listener);
        scrollView.onLayout(true, 0, 10, 0, height + 10);
        verify(listener, never()).onChildViewVisibilityChanged(eq(0), (View) any(), eq(true));

        reset(listener);
        scrollView.onLayout(true, 0, 51, 0, height + 51);
        verify(listener, only()).onChildViewVisibilityChanged(eq(0), (View) any(), eq(false));

        reset(listener);
        scrollView.onLayout(true, 0, 10, 0, height + 10);
        verify(listener, only()).onChildViewVisibilityChanged(eq(0), (View) any(), eq(true));
    }

    private View getMockView(int top, int bottom) {
        View view = mock(View.class);
        when(view.getTop()).thenReturn(top);
        when(view.getBottom()).thenReturn(bottom);

        return view;
    }

    @Test
    public void onLayoutNoChild() throws Exception {
        NotifiedScrollView scrollView = new NotifiedScrollView(new MockContext()) {
            @NonNull
            @Override
            public View getChildAt(int index) {
                return new FrameLayout(new MockContext());
            }
        };

        NotifiedScrollView.OnChildViewVisibilityChangedListener listener = mock
                (NotifiedScrollView.OnChildViewVisibilityChangedListener.class);
        scrollView.setOnChildViewVisibilityChangedListener(listener);
        scrollView.onScrollChanged(0, 0, 0, 0);

        verify(listener, never()).onChildViewVisibilityChanged(anyInt(), (View) any(), anyBoolean
                ());
    }

    @Test
    public void isViewInVisibleArea() throws Exception {
        NotifiedScrollView scrollView = new NotifiedScrollView(new MockContext());

        int parentTop = 50;
        int parentBottom = 100;

        assertFalse(scrollView.isViewInVisibleArea(parentTop, parentBottom, 0, 0));
        assertFalse(scrollView.isViewInVisibleArea(parentTop, parentBottom, 0, 49));
        assertFalse(scrollView.isViewInVisibleArea(parentTop, parentBottom, 101, 102));

        assertFalse(scrollView.isViewInVisibleArea(parentTop, parentBottom, 51, 49));

        assertTrue(scrollView.isViewInVisibleArea(parentTop, parentBottom, 0, 50));
        assertTrue(scrollView.isViewInVisibleArea(parentTop, parentBottom, 0, 51));

        assertTrue(scrollView.isViewInVisibleArea(parentTop, parentBottom, 51, 60));

        assertTrue(scrollView.isViewInVisibleArea(parentTop, parentBottom, 51, 100));
        assertTrue(scrollView.isViewInVisibleArea(parentTop, parentBottom, 51, 101));

    }

    @Test
    public void checkRatio() throws Exception {
        NotifiedScrollView scrollView = new NotifiedScrollView(new MockContext());
        scrollView.setPercentOfVisibility(50);

        int parentTop = 50;
        int parentBottom = 150;

        assertTrue(scrollView.isViewInVisibleArea(parentTop, parentBottom, 50, 75));

        assertFalse(scrollView.isViewInVisibleArea(parentTop, parentBottom, 20, 70));


        assertFalse(scrollView.isViewInVisibleArea(parentTop, parentBottom, 42, 51));
    }

    @Test
    public void getInVisibleArea() throws Exception {
        NotifiedScrollView scrollView = new NotifiedScrollView(new MockContext());

        int parentTop = 50;
        int parentBottom = 100;

        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 0, 0), 0);

        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 26, 75), 25);
        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 50, 75), 25);
        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 51, 75), 24);

        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 75, 100), 25);

        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 25, 100), 50);
        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 25, 200), 50);

        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 75, 200), 25);

        assertEquals(scrollView.getInVisibleArea(parentTop, parentBottom, 175, 200), 0);

    }

    private static class StubViewGroup extends ViewGroup {
        @NonNull
        private List<View> views = new ArrayList<>();

        public StubViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public void addView(View child) {
            views.add(child);
        }

        @Override
        public int getChildCount() {
            return views.size();
        }

        @Override
        public View getChildAt(int index) {
            return views.get(index);
        }
    }

}