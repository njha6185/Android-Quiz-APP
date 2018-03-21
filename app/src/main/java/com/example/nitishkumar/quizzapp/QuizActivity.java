package com.example.nitishkumar.quizzapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    String [] QUESTIONS = {
            "Sachin Tendulkar is Related to which Sports?",
            "Radium Was invented by which Scientists?",
            "When was Telephone Invented?",
            "Who recieved Bharat ratna before becoming president of India?",
            "Which is not writeen by Munshi Premchand?",
            "Which of the following app is developed in Android Studio?",
            "Who is author of Harry Potter series?",
            "Iron Man Movie belongs to Which studio?",
            "What is Capital of India?",
            "Which View Adds Image in Android Studio?"
    };

    String [][] ANSWERS = {
            {"Cricket","Football","Hockey","Tennis"},
            {"Isaac Newton", "Albert Einstein", "Franklin", "Marie Curie"},
            {"1880", "1870", "1850", "1860"},
            {"Dr. Rajender Prasad", "Indira Gandhi", "Dr. Zakir Hussain", "Acharya"},
            {"Gaban", "Guide", "Godan", "Mansorovar"},
            {"IOS app", "Blackberry app","Android app", "All"},
            {"J.K.Rawling","Aishwarya Rai","Mamta bainerji","Rahul gandhi"},
            {"DC", "Marvel", "Balaji", "TVF"},
            {"Patna", "U.P.", "Delhi", "Pakistan"},
            {"TextView", "Button", "ScrollView", "ImageView"}
    };

    String [] Correct_Answers = {
            "Cricket", "Marie Curie", "1870", "Dr. Zakir Hussain", "Guide", "Android app", "J.K.Rawling", "Marvel", "Delhi", "ImageView"
    };

    ArrayList<String> recieved_answers;

    int QNID, no_of_correct_answer;
    boolean nextStatus, optionStatus;

    VideoView videoView, quiz_videoview;
    Uri uri, uri_quiz;
    int stop_position, quiz_stop_positin;
    TextView questionNo, questionText, option1, option2, option3, option4, nextQuestion;
    MediaPlayer buttonmediaCorrect;
    MediaPlayer buttonmediaWrong;
    ToneGenerator toneGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initTextViews();
        changeQuestionAndOptions(QNID);

        stop_position = 0;
        startService(new Intent(this, soundservice.class));
        videoView = (VideoView) findViewById(R.id.backgroundView);
        quiz_videoview = (VideoView) findViewById(R.id.quiz_logo);
        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.quiz_background_video);
        uri_quiz = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.quiz);
        videoView.setVideoURI(uri);
        quiz_videoview.setVideoURI(uri_quiz);
        videoView.start();
        quiz_videoview.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
                quiz_videoview.start();
            }
        });
    }

    @Override
    protected void onPause() {
        Intent intent = new Intent(QuizActivity.this, soundservice.class);
        stopService(intent);
        stop_position = videoView.getCurrentPosition();
        videoView.pause();
        quiz_stop_positin = quiz_videoview.getCurrentPosition();
        quiz_videoview.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(QuizActivity.this, soundservice.class);
        startService(intent);
        videoView.seekTo(stop_position);
        videoView.start();
        quiz_videoview.seekTo(quiz_stop_positin);
        quiz_videoview.start();
        super.onResume();
    }

    /**************it initialises all the variables when quizActivity gets started***************/
    public void initTextViews()
    {
        no_of_correct_answer = 0;
        QNID = 0;
        nextStatus = false;
        optionStatus = true;
        recieved_answers = new ArrayList<String>();
        questionNo = (TextView)findViewById(R.id.quiz_question_no);
        questionText = (TextView)findViewById(R.id.quiz_question_text);
        option1 = (TextView)findViewById(R.id.opt1);
        option2 = (TextView)findViewById(R.id.opt2);
        option3 = (TextView)findViewById(R.id.opt3);
        option4 = (TextView)findViewById(R.id.opt4);
        nextQuestion = (TextView)findViewById(R.id.next_quest);
        buttonmediaCorrect = MediaPlayer.create(this, R.raw.correct_answer_button_sound);
        buttonmediaWrong = MediaPlayer.create(this, R.raw.fail_button_sound);
        buttonmediaCorrect.start();
        buttonmediaWrong.start();
        buttonmediaCorrect.seekTo(0);
        buttonmediaCorrect.pause();
        buttonmediaWrong.seekTo(0);
        buttonmediaWrong.pause();
    }

    /*******************it changes question and set it to next question on button press***********/
    public void changeQuestionAndOptions(int qnId)
    {
        int tempqnId = qnId+1;
        questionNo.setText(tempqnId + getString(R.string.slash) + QUESTIONS.length);
        questionText.setText(QNID+1 + getString(R.string.question_no_dotandgap) + QUESTIONS[qnId]);
        option1.setText(ANSWERS[qnId][0] );
        option2.setText(ANSWERS[qnId][1] );
        option3.setText(ANSWERS[qnId][2] );
        option4.setText(ANSWERS[qnId][3] );
    }

    /****************when first option is pressed**********/
    public void optionONE(View v)
    {
        if (optionStatus)
        {
            vibrateOnPress();
            QNID++;
            recieved_answers.add(option1.getText().toString());
            nextStatus = true;
//            Toast.makeText(this, "option1", Toast.LENGTH_LONG).show();
            optionStatus = false;
            if ( Correct_Answers[QNID-1].equalsIgnoreCase(recieved_answers.get(QNID-1) ) )
            {
                no_of_correct_answer += 1;
                option1.setBackgroundColor(Color.GREEN);
                buttonmediaCorrect.start();
//                Toast.makeText(this, "correct", Toast.LENGTH_LONG).show();
            }
            else
            {
                option1.setBackgroundColor(Color.RED);
                buttonmediaWrong.start();
//               Toast.makeText(this, "incorrect", Toast.LENGTH_LONG).show();
            }
        }
    }

    /****************when second option is pressed**********/
    public void optionTWO(View v)
    {
        if (optionStatus)
        {
            if (optionStatus)
            {
                vibrateOnPress();
                QNID++;
                recieved_answers.add(option2.getText().toString());
                nextStatus = true;
//            Toast.makeText(this, "option3", Toast.LENGTH_LONG).show();
                optionStatus = false;
                if ( Correct_Answers[QNID-1].equalsIgnoreCase(recieved_answers.get(QNID-1) ) )
                {
                    no_of_correct_answer += 1;
                    option2.setBackgroundColor(Color.GREEN);
                    buttonmediaCorrect.start();
//                Toast.makeText(this, "correct", Toast.LENGTH_LONG).show();
                }
                else
                {
                    option2.setBackgroundColor(Color.RED);
                    buttonmediaWrong.start();
//                Toast.makeText(this, "incorrect", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /****************when third option is pressed**********/
    public void optionTHREE(View v)
    {
        if (optionStatus)
        {
            vibrateOnPress();
            QNID++;
            recieved_answers.add(option3.getText().toString());
            nextStatus = true;
//            Toast.makeText(this, "option3", Toast.LENGTH_LONG).show();
            optionStatus = false;
            if ( Correct_Answers[QNID-1].equalsIgnoreCase(recieved_answers.get(QNID-1) ) )
            {
                no_of_correct_answer += 1;
                option3.setBackgroundColor(Color.GREEN);
                buttonmediaCorrect.start();
//                Toast.makeText(this, "correct", Toast.LENGTH_LONG).show();
            }
            else
            {
                option3.setBackgroundColor(Color.RED);
                buttonmediaWrong.start();
//                Toast.makeText(this, "incorrect", Toast.LENGTH_LONG).show();
            }
        }
    }
    /****************when fourth option is pressed**********/

    public void optionFOUR(View v)
    {
        if (optionStatus)
        {
            vibrateOnPress();
            QNID++;
            recieved_answers.add(option4.getText().toString());
            nextStatus = true;
//            Toast.makeText(this, "option4", Toast.LENGTH_LONG).show();
            optionStatus = false;
            if ( Correct_Answers[QNID-1].equalsIgnoreCase(recieved_answers.get(QNID-1) ) )
            {
                no_of_correct_answer += 1;
                option4.setBackgroundColor(Color.GREEN);
                buttonmediaCorrect.start();
//                Toast.makeText(this, "correct", Toast.LENGTH_LONG).show();
            }
            else
            {
                option4.setBackgroundColor(Color.RED);
                buttonmediaWrong.start();
//                Toast.makeText(this, "incorrect", Toast.LENGTH_LONG).show();
            }
        }
    }

    /****************when next button is pressed**********/
    public void next_question(View view)
    {
        //Toast.makeText(this,String.valueOf(QNID),Toast.LENGTH_SHORT).show();
        if (nextStatus)
        {
            changeTextColor();
            beepSound();
            vibrateOnPress();
            if( QNID == 10 )
            {
                QNID = 9;
                popupmenu();
                recieved_answers.clear();
            }

//            Toast.makeText(this, recieved_answers.get(QNID-1), Toast.LENGTH_LONG).show();
//            Toast.makeText(this, "next", Toast.LENGTH_LONG).show();
            changeQuestionAndOptions(QNID);
            nextStatus = false;
            optionStatus = true;
            buttonmediaCorrect.seekTo(0);
            buttonmediaCorrect.pause();
            buttonmediaWrong.seekTo(0);
            buttonmediaWrong.pause();
            toneGenerator.release();
        }
    }
    /****************when next button is pressed. it helps in generating abbep sound**********/
    public void beepSound()
    {
        toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM,70);
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 120);
    }

    /****************when any button or option is pressed. it helps in generating vibration**********/
    public void vibrateOnPress()
    {
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
    /****************when next option is pressed. it helps in changing option color depending on right or wrong answer.
     *  Red->wrong, green->right**********/
    public void changeTextColor()
    {
        option1.setBackgroundColor(Color.TRANSPARENT);
        option1.setBackgroundResource(R.drawable.set_border);
        option2.setBackgroundColor(Color.TRANSPARENT);
        option2.setBackgroundResource(R.drawable.set_border);
        option3.setBackgroundColor(Color.TRANSPARENT);
        option3.setBackgroundResource(R.drawable.set_border);
        option4.setBackgroundColor(Color.TRANSPARENT);
        option4.setBackgroundResource(R.drawable.set_border);
    }
    /****************It prings the alert dialog popup menu when quiz gets over**********/
    public void popupmenu()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        View view = getLayoutInflater().inflate(R.layout.end_popup, null);
        TextView popup_textview = (TextView)view.findViewById(R.id.popuptextdisplay);
        popup_textview.setText(scoreMessage());
        Button retry_button = (Button)view.findViewById(R.id.retrybutton);
        Button close_button = (Button)view.findViewById(R.id.closebutton);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    /****************when popup close button is pressed. closes all activity and exit app**********/
    public void closeQuiz(View v)
    {
        buttonmediaWrong.release();
        buttonmediaCorrect.release();
        finish();
    }

    /****************when popup restart button is pressed. closes all activity and restart app**********/
    public void retryQuiz(View v)
    {
        buttonmediaWrong.release();
        buttonmediaCorrect.release();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /****************It generate score card message at the end of Quiz**********/

    public String scoreMessage()
    {
        String message=getString(R.string.message_part1)+ String.valueOf(no_of_correct_answer) + getString(R.string.message_part2);
        return message;
    }
}