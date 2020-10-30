package com.example.soundcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LetterGameActivity extends AppCompatActivity implements LetterGameContract.View{

    private final static String REPOSITORY = "REPOSITORY";
    private final static String GAME_TYPE = "GAME_TYPE";
    private final static String BASIC_GAME = "BASIC_GAME";
    private final static String TEST_GAME = "TEST_GAME";

    Button btn1;
    Button btn2;
    Button btn3;
    TextView tvCounter;
    private Boolean isTeachingModel;
    private boolean isScaled = false;
    private SharedPreferences sharedPrefs;

    LetterGamePresenter presenter;
    private LayoutScalerUtility layoutScalerUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_game);

        tvCounter = findViewById(R.id.tv_win_counter);
        btn1 = findViewById(R.id.btn_option1);
        btn2 = findViewById(R.id.btn_option2);
        btn3 = findViewById(R.id.btn_option3);

        sharedPrefs = this.getSharedPreferences(REPOSITORY, this.MODE_PRIVATE);
        String gameType = sharedPrefs.getString(GAME_TYPE, null);

        presenter = new LetterGamePresenter(this);

        //scale layout
        layoutScalerUtility = new LayoutScalerUtility();

        //switch for game type
        switch (gameType){
            case BASIC_GAME:
                isTeachingModel = true;
                break;
            case TEST_GAME:
                presenter.loadTest();
                isTeachingModel = false;
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(!isScaled) {
            layoutScalerUtility.scaleContents(findViewById(R.id.letter_game_contents), findViewById(R.id.letter_game_container));
            if(findViewById(R.id.letter_game_contents).getLayoutParams().width !=  findViewById(R.id.letter_game_container).getWidth()){
                findViewById(R.id.letter_game_contents).getLayoutParams().width =  findViewById(R.id.letter_game_container).getWidth();
            }
            isScaled = true;
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openTeachingModel(char letter){
        //TODO find better way to load buttons and play sound
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_teaching_model, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button btnOnlyAnswer = popupView.findViewById(R.id.btn_only_option);
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

        ImageButton btnSound = popupView.findViewById(R.id.btn_PlaySound);
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(v);
            }
        });

        //Set the location of the window on the screen
        popupWindow.showAtLocation(findViewById(R.id.letter_game_contents), Gravity.CENTER, 0, 0);

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
        tvCounter.setText(String.valueOf(count));
        if(count == -1){
            tvCounter.setText("");
        }
        else{
            tvCounter.setText(String.valueOf(count));
        }
        // if @3 celebration animation
        if(count == 2){
            Intent intent = new Intent(this, LetterMenuActivity.class);
            setResult(2, intent);
            finish();
            return false;
        }
        return true;
    }

    public void openTestAlert(){
        TestAlertDialog testAlertDialog = new TestAlertDialog();
        testAlertDialog.show(getSupportFragmentManager(), "test alert dialog");
    }

    public void finishTest(){
        finish();
    }
}
