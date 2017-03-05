package mobi.tarantino.stub.auto.helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**

 */

public class AnimationHelper {
    public static void rotateWithAlpha(int duration, float rotateFrom,
                                       float rotateTo, float alphaFrom, float alphaTo,
                                       final View target) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(target, "rotation", rotateFrom, rotateTo);
        rotate.setDuration(duration);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", alphaFrom, alphaTo);
        alpha.setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotate, alpha);
        animatorSet.start();

    }
}
