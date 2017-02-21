package com.example.stoycho.phonebook.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by stoycho.petrov on 20/02/2017.
 */

public class BaseActivity extends FragmentActivity {

    public void setFocusOfEditText(EditText view, final TextView textView) {
        if (view.getText().toString().equals("")) {
            ValueAnimator translationX = ObjectAnimator.ofFloat(textView, "translationY", textView.getTranslationY(), textView.getTranslationY() - 50);
            ValueAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 0.8f);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 1f, 0.8f);
            textView.setPivotX(0);
            textView.setPivotY(0);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translationX, scaleX, scaleY);
            animatorSet.start();

            textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        }
    }

    public void removeFocus(EditText view,TextView textView)
    {
        if(view.getText().toString().equals("")) {
            ValueAnimator translationY = ObjectAnimator.ofFloat(textView, "translationY", textView.getTranslationY(), textView.getTranslationY() + 50);
            ValueAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 0.8f, 1f);
            ValueAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 0.8f, 1f);
            textView.setPivotX(0);
            textView.setPivotY(0);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translationY, scaleX, scaleY);
            animatorSet.start();

            textView.setTextColor(ContextCompat.getColor(this,android.R.color.black));
        }
    }
}
