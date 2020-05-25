package com.example.soundcard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LetterMenu extends AppCompatActivity  {

    static SoundManager soundManager;
    static UserManager userManager;
    static ButtonManager buttonManager;

    private Button currentButton;
    private Case currentCase = Case.UPPERCASE;
    private int consecutiveWinsTestCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_menu);

        Bundle bundle = getIntent().getExtras();
        userManager = new UserManager(this, bundle.getString("current_user"));
        Case c = (Case)bundle.getSerializable("starting_case");

        consecutiveWinsTestCounter = 0;

        buttonManager = new ButtonManager(findViewById(android.R.id.content));
        Log.d("TESTING", "getuserletters: " + userManager.getUserLetters(currentCase));
        buttonManager.loadUserButtons(userManager.getUserLetters(currentCase));
        swapToCase(c);

        soundManager = new SoundManager(this);
    }

    public void startTest(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("user_letters", userManager.getUserLetters(currentCase));
        intent.putExtra("game_type", "test_game");
        intent.putExtra("starting_case", currentCase);
        startActivityForResult(intent, 0);
    }

    public void selectLetter(View v){
        Button b = (Button)v;
        currentButton = b;
        Intent intent = new Intent(v.getContext(), GameActivity.class);
        intent.putExtra("current_char",b.getText().charAt(0));
        intent.putExtra("game_type", "basic_game");
        intent.putExtra("starting_case", currentCase);
        startActivityForResult(intent, 0);
    }

    public void clearUser(View v){
        buttonManager.resetButtons(userManager.getUserLetters(currentCase));
        userManager.clearUser(this, currentCase);
    }

    public void swapCase(View v){
        Button b = (Button)v;
        if(b.getId() == R.id.BUTTON_UPPERCASE){
            swapToCase(Case.UPPERCASE);
        }
        else if(b.getId() == R.id.BUTTON_LOWERCASE){
            swapToCase(Case.LOWERCASE);
        }
    }

    public void swapToCase(Case myCase){
        if(myCase == Case.UPPERCASE && currentCase != myCase){
            currentCase = Case.UPPERCASE;
            buttonManager.swapToCase(Case.UPPERCASE, userManager.getUserLetters(currentCase));
        }
        else if(myCase == Case.LOWERCASE && currentCase != myCase){
            currentCase = Case.LOWERCASE;
            buttonManager.swapToCase(Case.LOWERCASE, userManager.getUserLetters(currentCase));
        }
        else if(myCase == Case.BOTH && currentCase != myCase){
            currentCase = Case.BOTH;
            buttonManager.swapToCase(Case.BOTH, userManager.getUserLetters(currentCase));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            String gameType = data.getStringExtra("game_type");
            if(gameType.equals("basic_game")){
                char c = data.getCharExtra("buttonName", '?');
                Log.d("TESTING", "comparing: " + c + " and " + currentButton.getText().charAt(0));
                if(c == currentButton.getText().charAt(0) || c == Character.toLowerCase(currentButton.getText().charAt(0))){
                    buttonManager.loadUserButtons(Character.toString(c));
                    userManager.addUserLetter(this, c, currentCase);
                    consecutiveWinsTestCounter++;
                    if(consecutiveWinsTestCounter == 2){
                        startTest();
                    }
                }
            }
            else if(gameType.equals("test_game")){
                String correctAns = data.getStringExtra("correct_answers");
                String wrongAns = data.getStringExtra("wrong_answers");
                Boolean isSounds = false;
                if(currentCase == Case.BOTH){
                    isSounds = true;
                }
                userManager.saveTestResults(this, correctAns, wrongAns, isSounds);
                buttonManager.resetButtons(wrongAns);
                consecutiveWinsTestCounter = 0;
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        soundManager.onDestroy();
    }
}
