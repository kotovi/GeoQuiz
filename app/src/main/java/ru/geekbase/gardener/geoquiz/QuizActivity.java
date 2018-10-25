package ru.geekbase.gardener.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private ImageButton mResumeImageButton;
    private ImageButton mNextImageButton;

    private static String Tag = "ActivityQuiz";
    private static final String KEY_INDEX_ANSWERS_ARRAY = "index_answers_Array";
    private static final String KEY_INDEX = "index";
    private static final String SCORE_INDEX ="score";
    private static final String KEY_CHEATER = "cheaterKey";
    private static final String KEY_CHEATED_QUESTIONS="index_cheatered_questions";
    private static final int REQUEST_CODE_CHEAT = 0;

    private TextView mQuestionTextViev;
    private TextView mScoreTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_australia,  false),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_russia, true)
    };

    //попробуем реализовать блокировку уже отвеченных вопросов
    int mQuestionBankLeng = mQuestionBank.length;
    boolean [] AnsweredQuestion = new boolean[mQuestionBankLeng];
    boolean [] cheatedQuestion = new boolean[mQuestionBankLeng];

    private int mCurrentIndex = 0;
    private int score = 0;
    private boolean mIsCheater = false;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(Tag, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_INDEX_ANSWERS_ARRAY, AnsweredQuestion);
        savedInstanceState.putInt(SCORE_INDEX,score);
        savedInstanceState.putBoolean(KEY_CHEATER,mIsCheater);
        savedInstanceState.putBooleanArray(KEY_CHEATED_QUESTIONS,cheatedQuestion);
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
          AnsweredQuestion = savedInstanceState.getBooleanArray(KEY_INDEX_ANSWERS_ARRAY);
          score = savedInstanceState.getInt(SCORE_INDEX,0);
          mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER,false);
          cheatedQuestion = savedInstanceState.getBooleanArray(KEY_CHEATED_QUESTIONS);
          showScore();
        }
        //
        mQuestionTextViev = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextViev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        //
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        //
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });
        //
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                Boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent =CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        //
        mResumeImageButton = (ImageButton) findViewById(R.id.resume_image_button);
        mResumeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        //
        mNextImageButton = (ImageButton) findViewById(R.id.next_image_button);
        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if (data==null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextViev.setText(question);
    }


    private void showScore() {
        mScoreTextView = (TextView) findViewById(R.id.score);
        mScoreTextView.setText(String.valueOf(score));

    }

    private void checkAnswer (boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
         if (cheatedQuestion[mCurrentIndex]) mIsCheater =true;
        if (AnsweredQuestion[mCurrentIndex]) { messageResId=R.string.already_answered; }
            else {
                if (mIsCheater) {
                    messageResId = R.string.judgment_toast;
                    cheatedQuestion[mCurrentIndex] = true;

                }
            else {

                if (userPressedTrue == answerIsTrue) {
                    score = score + 10;
                    AnsweredQuestion[mCurrentIndex] = true;
                    messageResId = R.string.correct_toast;
                    } else {
                      AnsweredQuestion[mCurrentIndex] = true;
                      messageResId = R.string.incorrect_toast;
                      }
            }
        }

            showScore();

        }




}
