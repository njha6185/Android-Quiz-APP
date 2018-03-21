package com.example.nitishkumar.quizzapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.media.JetPlayer;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView image1;
    Button playButtonView;
    TextView msg_text, msg_text2;
    int transitionTime;
    boolean activatePlauButton;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transitionTime = 1500;
        image1 = (ImageView) findViewById(R.id.imageView);
        playButtonView = (Button) findViewById(R.id.play_button);
        msg_text = (TextView) findViewById(R.id.msgText);
        msg_text2 = (TextView) findViewById(R.id.msgText2);
        activatePlauButton = true;
        mediaPlayer = MediaPlayer.create(this, R.raw.start_screenbackground_sound);
    }

    /**********start_quiz function Start Animations When Play Quiz Button is pressod*************************/

    public void start_quiz(View v) {
        if (activatePlauButton) {
            mediaPlayer.start();
            playButtonView.setBackgroundResource(R.drawable.switchon);
            playButtonView.setRotation(1);
            ((TransitionDrawable) image1.getDrawable()).startTransition(transitionTime);
            msg_text.setText(R.string.welcome_screen_head1);
            msg_text2.setText(R.string.welcome_screen_head2);
            msg_text.setTextColor(Color.parseColor(getString(R.string.welcome_screen_java_text_color)));
            msg_text2.setTextColor(Color.parseColor(getString(R.string.welcome_screen_java_text_color)));
            ObjectAnimator animation = ObjectAnimator.ofFloat(msg_text, "translationY", 530f);
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(msg_text2, "translationY", -650f);
            ObjectAnimator animation2 = ObjectAnimator.ofInt(msg_text, "textColor", Color.RED, Color.GREEN);
            animation2.setEvaluator(new ArgbEvaluator());
            ObjectAnimator animation3 = ObjectAnimator.ofInt(msg_text2, "textColor", Color.RED, Color.GREEN);
            animation3.setEvaluator(new ArgbEvaluator());

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animation, animation1, animation2, animation3);
            animatorSet.setDuration(transitionTime);
            animatorSet.start();
            activatePlauButton = false;

            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    launchQuizActivity();
                    mediaPlayer.stop();
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    /************ this function will launch* Quiz activity ****************/

    public void launchQuizActivity() {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }
}