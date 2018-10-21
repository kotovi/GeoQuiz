package ru.geekbase.gardener.geoquiz;

import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private ImageButton mResumeImageButton;
    private ImageButton mNextImageButton;

    private static String Tag = "ActivityQuiz";
    private static final String KEY_INDEX = "index";

    private TextView mQuestionTextViev;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_australia,  false),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_russia, true)
    };

    private int mCurrentIndex = 0;
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(Tag, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(Tag, "onPause() called");
    }
    @Override
    public void onStop() {

        super.onStop();
        Log.d(Tag, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Tag, "onDestroy() called");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tag,"onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState!=null) {
          mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }

        mQuestionTextViev = (TextView) findViewById(R.id.question_text_view);

        mQuestionTextViev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });



        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });



        mResumeImageButton = (ImageButton) findViewById(R.id.resume_image_button);
        mResumeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mNextImageButton = (ImageButton) findViewById(R.id.next_image_button);
        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextViev.setText(question);
    }

    private void checkAnswer (boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
