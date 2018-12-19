package com.omelchenkoaleks.budgetcontrol.transitions;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.transition.Slide;
import android.view.Gravity;

// содержит реализации по анимации переходов между активити
public class TransitionSlide {

    private Direction direction;

    public enum Direction{
        RIGHT_LEFT, LEFT_RIGHT, TOP_BOTTOM, BOTTOM_TOP
    }

    private Activity activity;

    public TransitionSlide(Activity activity, Direction direction) {
        this.activity = activity;
        this.direction = direction;
        setupAnimation();
    }



    public void finishWithTransition() {
        ActivityCompat.finishAfterTransition(activity);
    }


    private void setupAnimation() {

        Slide slide = null;
        Slide slide2 = null;

        switch (direction){
            case RIGHT_LEFT:

                // при открытии активити
                slide = new Slide(Gravity.RIGHT);
                slide.setDuration(300);
                activity.getWindow().setEnterTransition(slide);

                // при закрытии активити
                slide2 = new Slide(Gravity.LEFT);
                slide2.setDuration(300);
                activity.getWindow().setExitTransition(slide2);

                break;

            case LEFT_RIGHT:

                // при открытии активити
                slide = new Slide(Gravity.LEFT);
                slide.setDuration(300);
                activity.getWindow().setEnterTransition(slide);

                // при закрытии активити
                slide2 = new Slide(Gravity.RIGHT);
                slide2.setDuration(300);
                activity.getWindow().setExitTransition(slide2);

                break;

            case TOP_BOTTOM:

                // при открытии активити
                slide = new Slide(Gravity.TOP);
                slide.setDuration(300);
                activity.getWindow().setEnterTransition(slide);

                // при закрытии активити
                slide2 = new Slide(Gravity.BOTTOM);
                slide2.setDuration(300);
                activity.getWindow().setExitTransition(slide2);
                break;


            case BOTTOM_TOP:

                // при открытии активити
                slide = new Slide(Gravity.BOTTOM);
                slide.setDuration(300);
                activity.getWindow().setEnterTransition(slide);

                // при закрытии активити
                slide2 = new Slide(Gravity.TOP);
                slide2.setDuration(300);
                activity.getWindow().setExitTransition(slide2);
                break;
        }




    }

}
