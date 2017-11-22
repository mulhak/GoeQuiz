

package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;
// Chapter 1-2-3 Big Nerd Ranch book
//location desktop folder

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;


    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.Question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private int mCorrect= 0;
    private int mIncorrect=0;
    private boolean mIsCheater;

    private void checkFinish() {
        if (mQuestionBank.length== (mCorrect + mIncorrect)) {
           Toast.makeText(QuizActivity.this, "That's all!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState != null) {
            mCurrentIndex= savedInstanceState.getInt(KEY_INDEX, 0);
        }


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCurrentIndex= (mCurrentIndex +1) % mQuestionBank.length;
            updateQuestion();
        }
    });
// these lines replaced by updateQuestion() method
    // int question = mQuestionBank[mCurrentIndex].getTextResId();
    // mQuestionTextView.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkAnswer(true);
            mFalseButton.setEnabled(false);
            mCorrect= mCorrect+1;
        }
    });

    mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkAnswer(false);
          mTrueButton.setEnabled(false);
            mIncorrect= mIncorrect+1;
        }
    });


    mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            mIsCheater = false;
            updateQuestion();
            checkFinish();

        }
    });
        // put checkAnswer() here to allow variable to be seen???




        mCheatButton= (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View view) {
               //Start CheatActivity-- new way takes out first line below
              // Intent intent = new Intent(QuizActivity.this, CheatActivity.class );
               boolean answerIsTrue= mQuestionBank[mCurrentIndex].isAnswerTrue();
               Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
               startActivityForResult(intent, REQUEST_CODE_CHEAT);
           }
        });

        updateQuestion();

        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCurrentIndex=(mCurrentIndex - 1)% mQuestionBank.length;

            updateQuestion();
        }
    });
    updateQuestion();

}
@Override
public void onSaveInstanceState(Bundle savedInstanceState){
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);

}

        @Override
         protected void onActivityResult(int requestCode, int resultCode , Intent data ){
        if (requestCode != Activity.RESULT_OK){
        return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
        if(data==null){
        return;
        }
        mIsCheater= CheatActivity.wasAnswerShown(data);
        }
        }

     // these lines replaced by updateQuestion() method
    // int question = mQuestionBank[mCurrentIndex].getTextResId();
    // mQuestionTextView.setText(question);


    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mFalseButton.setEnabled(true);
        mTrueButton.setEnabled(true);

    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId= 0;
        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{

            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
            } else{
                messageResId = R.string.incorrect_toast;
            }
            makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
    }

}














