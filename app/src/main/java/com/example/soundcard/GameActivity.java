package com.example.soundcard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements GameContract.View{

    Button btn1;
    Button btn2;
    Button btn3;
    TextView theCorrectAnsTextView;
    TextView counter;
    private Boolean isTeachingModel;

    GamePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        theCorrectAnsTextView = findViewById(R.id.ShowLetter);
        counter = findViewById(R.id.WinCounter);
        btn1 = findViewById(R.id.Option1);
        btn2 = findViewById(R.id.Option2);
        btn3 = findViewById(R.id.Option3);

        Bundle bundle = getIntent().getExtras();
        String gameType = bundle.getString("game_type");


        presenter = new GamePresenter(this);

        //break for game type
        if (gameType.equals("basic_game")) {
            isTeachingModel = true;
        } else if (gameType.equals("test_game")) {
            presenter.loadTest();
            isTeachingModel = false;
        }
    }


    @Override
    public void onAttachedToWindow() {
        if(isTeachingModel){
            super.onAttachedToWindow();
            openTeachingModel(presenter.getCurrentLetter());
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
                presenter.loadBasicGame();
            }
        });
        btnOnlyAnswer.setAllCaps(false);
        if(presenter.getCase() == Case.BOTH){
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
        popupWindow.showAtLocation(findViewById(R.id.content_game), Gravity.CENTER, 0, 0);

        presenter.playSound();
    }

    public void playSound(View v){
        presenter.playSound();
    }

    public void checkAnswer(View v){
        char c = ((Button)v).getText().charAt(0);
        presenter.checkAnswer(c);
    }

    public void updateButtons(char nChar1, char nChar2, char nChar3){
        Log.d("TESTING", "update buttons");
        btn1.setText(String.valueOf(nChar1));
        btn2.setText(String.valueOf(nChar2));
        btn3.setText(String.valueOf(nChar3));
        presenter.playSound();
    }

    // returns true if continue
    public Boolean updateWinCounter(int count){
        counter.setText(String.valueOf(count));
        // if @3 celebration animation
        if(count == 2){
            Intent intent = new Intent(this, LetterSelectActivity.class);
            setResult(2, intent);
            finish();
            return false;
        }
        return true;
    }

    public void finishTest(String correctAns, String wrongAns){
        finish();
    }

    public void showLetter(char c){
        theCorrectAnsTextView.setText(String.valueOf(c));
    }
}
