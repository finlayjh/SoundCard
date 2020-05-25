package com.example.soundcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.String;

public class GameActivity extends AppCompatActivity implements GameManager.GameManagerInterface {

    Button btn1;
    Button btn2;
    Button btn3;
    TextView theCorrectAnsTextView;
    TextView counter;
    private Boolean isTeachingModel;
    private Case currentCase;

    private SoundManager soundManager;
    private GameManager gameManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        String gameType = bundle.getString("game_type");
        currentCase = (Case)bundle.getSerializable("starting_case");

        //store references
        theCorrectAnsTextView = findViewById(R.id.ShowLetter);
        counter = findViewById(R.id.WinCounter);
        btn1 = findViewById(R.id.Option1);
        btn2 = findViewById(R.id.Option2);
        btn3 = findViewById(R.id.Option3);

        soundManager = LetterMenu.soundManager;
        gameManager = new GameManager(this);

        //break for game type
        if(gameType.equals("basic_game")){
            char currentLetter = bundle.getChar("current_char");
            gameManager.setCurrentLetter(currentLetter);
            isTeachingModel = true;
        }else if(gameType.equals("test_game")){
            String userLetters = bundle.getString("user_letters");
            gameManager.loadTest(userLetters);
            isTeachingModel = false;
        }
    }

    @Override
    public void onAttachedToWindow() {
        if(isTeachingModel){
            super.onAttachedToWindow();
            openTeachingModel(gameManager.getCurrentLetter());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openTeachingModel(char letter){
        //TODO find better way to load buttons and play sound
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.content_teaching_model, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView cLetter = popupView.findViewById(R.id.TEXTVIEW_TM_LETTER);
        cLetter.setText(String.valueOf(letter));

        Button btnOnlyAnswer = popupView.findViewById(R.id.BUTTON_TM_ONLY_ANSWER);
        btnOnlyAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                gameManager.loadBasicGame();
            }
        });
        btnOnlyAnswer.setAllCaps(false);
        if(currentCase == Case.BOTH){
            btnOnlyAnswer.setText(String.valueOf(letter) + String.valueOf((char)(letter+32)));
        }
        else{
            btnOnlyAnswer.setText(String.valueOf(letter));
        }

        Button btnSound = popupView.findViewById(R.id.BUTTON_TM_SOUND);
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(v);
            }
        });

        //Set the location of the window on the screen
        popupWindow.showAtLocation((ConstraintLayout) findViewById(R.id.content_game), Gravity.CENTER, 0, 0);

        soundManager.playSound();
    }

    public void playSound(View v){
        soundManager.playSound();
    }

    public void checkAnswer(View v){
        char c = ((Button)v).getText().charAt(0);
        gameManager.checkAnswer(c);
    }

    public void updateButtons(char nText1, char nText2, char nText3){
        Log.d("TESTING", "update buttons");
        btn1.setText(String.valueOf(nText1));
        btn2.setText(String.valueOf(nText2));
        btn3.setText(String.valueOf(nText3));
        soundManager.playSound();
    }

    public boolean updateCounter(int count){
        counter.setText(String.valueOf(count));
        // if @3 celebration animation
        if(count == 2){
            Intent intent = new Intent(this, LetterMenu.class);
            intent.putExtra("game_type", "basic_game");
            intent.putExtra("buttonName", gameManager.getCurrentLetter());
            setResult(RESULT_OK, intent);
            finish();
            return false;
        }
        return true;
    }

    public void finishTest(String correctAns, String wrongAns){
        Intent intent = new Intent(this, LetterMenu.class);
        intent.putExtra("game_type", "test_game");
        intent.putExtra("correct_answers", correctAns);
        intent.putExtra("wrong_answers", wrongAns);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showLetter(char c){
        theCorrectAnsTextView.setText(String.valueOf(c));
    }

    public void setCurrentLetter(char c){
        soundManager.setCurrentLetter(c);
    }

    public Case getCurrentCase(){return currentCase;}
}
