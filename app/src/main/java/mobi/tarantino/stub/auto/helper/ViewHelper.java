package mobi.tarantino.stub.auto.helper;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Checkable;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class ViewHelper {
    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        if (viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = viewGroup.getChildAt(i);
                view.setEnabled(enabled);
                if (view instanceof ViewGroup) {
                    enableDisableViewGroup((ViewGroup) view, enabled);
                }
            }
        }
    }

    public static void checkAllViewGroup(ViewGroup viewGroup, boolean checked) {
        if (viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof Checkable) {
                    ((Checkable) view).setChecked(checked);
                }
                if (view instanceof ViewGroup) {
                    checkAllViewGroup((ViewGroup) view, checked);
                }
            }
        }
    }

    public static void hideKeyboard(View v, Context context) {
        if (context != null) {
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void setClickableAllViewGroup(ViewGroup viewGroup, boolean clickable) {
        if (viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = viewGroup.getChildAt(i);
                view.setClickable(clickable);
                if (view instanceof ViewGroup) {
                    setClickableAllViewGroup((ViewGroup) view, clickable);
                }
            }
        }
    }


    public static void setTextOrHide(TextView textView, String value) {
        if (!TextUtils.isEmpty(value)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(value);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public static void setTextOrHide(TextView textView, @StringRes int resId, Object... values) {
        if (values != null && values.length > 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(textView.getResources().getString(resId, values));
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap
                .Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static void changeBackgroundAnimated(@NotNull final View view,
                                                @ColorInt int colorFrom,
                                                @ColorInt int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom,
                colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    /**
     * Prevent dialog dismiss when orientation changes
     */

    public static void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }
}
