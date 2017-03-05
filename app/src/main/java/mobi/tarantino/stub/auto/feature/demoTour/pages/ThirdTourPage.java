package mobi.tarantino.stub.auto.feature.demoTour.pages;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mobi.tarantino.stub.auto.R;

/**

 */

public class ThirdTourPage extends BaseTourPage implements View.OnClickListener {

    private ImageView main;
    private TextView text;
    private TextView label;
    private Button nextButton;
    private boolean endAnimation = false;

    public ThirdTourPage(Context context, @LayoutRes int layout) {
        super(context, layout);
    }

    @Override
    public void initViews(View parent) {
        main = (ImageView) parent.findViewById(R.id.image_main);
        label = (TextView) parent.findViewById(R.id.label_textView);
        text = (TextView) parent.findViewById(R.id.text_textView);
        nextButton = (Button) parent.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
        resetAnimations();
    }

    private void resetAnimations() {
        label.setAlpha(0f);
        text.setAlpha(0f);
    }

    @Override
    public void animateVisibleOn() {
        if (!endAnimation) {
            animateLabel();
            animateTextInfo();
            animateCommon();
            endAnimation = !endAnimation;
        }
    }

    private void animateCommon() {
        Drawable drawable = main.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void animateTextInfo() {
        AnimatorSet discountTextAnimator = new AnimatorSet();
        discountTextAnimator.play(ObjectAnimator.ofFloat(text, "alpha", 0f, 1f));
        discountTextAnimator.setDuration(750l);
        discountTextAnimator.setStartDelay(500l);
        discountTextAnimator.start();
    }

    private void animateLabel() {
        AnimatorSet discountLabelAnimator = new AnimatorSet();
        discountLabelAnimator.play(ObjectAnimator.ofFloat(label, "alpha", 0f, 1f));
        discountLabelAnimator.setDuration(750l);
        discountLabelAnimator.start();
    }

    @Override
    public void onEntryAnimation(float position) {
        if (position > 0 && position < 1) {
            main.setScaleY(1 - position);
            main.setScaleX(1 - position);
        } else {
            main.setScaleY(1f);
            main.setScaleX(1f);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
