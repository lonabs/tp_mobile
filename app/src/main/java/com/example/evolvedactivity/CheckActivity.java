package com.example.evolvedactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckActivity extends AppCompatActivity {
    private Button cancelButton;
    private Button okButton;

    private TextView challengeNumber1TextView;
    private TextView challengeNumber2Textview;

    private EditText userAnswerEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        cancelButton = (Button) findViewById(R.id.cancelChallengeButton);
        okButton = (Button) findViewById(R.id.okChallengeButton);

        challengeNumber1TextView = (TextView) findViewById(R.id.challengeNumber1TextView);
        challengeNumber2Textview = (TextView) findViewById(R.id.challengeNumber2TextView);

        userAnswerEditText = (EditText) findViewById(R.id.challengeUserInputEditText);

        loadChallengeNumbers();

        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );

        okButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if (isUserAnswerCorrect()){
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            mainActivityIntent.putExtra("isChallengePassed", true);
                            setResult(Activity.RESULT_OK, mainActivityIntent);
                            finish();
                        }
                        else {
                            Toast.makeText(CheckActivity.this, "ANSWER INCORRECT", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    private void loadChallengeNumbers(){
        int challengeNumber1 = getIntent().getExtras().getInt("challengeNumber1");
        int challengeNumber2 = getIntent().getExtras().getInt("challengeNumber2");
        challengeNumber1TextView.setText(String.valueOf(challengeNumber1));
        challengeNumber2Textview.setText(String.valueOf(challengeNumber2));
    }

    private boolean isUserAnswerCorrect(){
        int number1 = Integer.parseInt(challengeNumber1TextView.getText().toString());
        int number2 = Integer.parseInt(challengeNumber2Textview.getText().toString());
        return number1+number2 == Integer.parseInt(userAnswerEditText.getText().toString());
    }

}