package ru.geekbase.gardener.geoquiz;

import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextViev = (TextView) findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextViev.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(QuizActivity.this, R.string.correct_toast,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.show();
                //описание действия
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(QuizActivity.this, R.string.incorrect_toast,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();

            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                int question = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextViev.setText(question);
            }
        });
    }
}
