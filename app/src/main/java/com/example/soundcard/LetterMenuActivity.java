package com.example.soundcard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LetterMenuActivity extends AppCompatActivity implements LetterMenuContract.View {

    private final static String REPOSITORY = "REPOSITORY";
    private final static String GAME_TYPE = "GAME_TYPE";
    private final static String BASIC_GAME = "BASIC_GAME";
    private final static String TEST_GAME = "TEST_GAME";

    LetterMenuPresenter presenter;

    TextView tvLessonType;
    private LayoutScalerUtility layoutScalerUtility;
    private boolean isScaled = false;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_menu);

        SoundManager soundManager = new SoundManager(this);
        ButtonManager buttonManager = new ButtonManager(findViewById(android.R.id.content));
        presenter = new LetterMenuPresenter(this, soundManager, buttonManager);
        Toolbar myToolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MainMenu");

        //scale layout
        layoutScalerUtility = new LayoutScalerUtility();

        tvLessonType = findViewById(R.id.tv_title);
        switch(presenter.getCase()) {
            case BOTH:
                tvLessonType.setText("Letter Sounds");
                break;
            default:
                tvLessonType.setText("Letter Names");
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(!isScaled) {
            layoutScalerUtility.scaleContents(findViewById(R.id.letter_menu_contents), findViewById(R.id.letter_menu_container));
            if(findViewById(R.id.letter_menu_contents).getLayoutParams().width !=  findViewById(R.id.letter_menu_container).getWidth()){
                findViewById(R.id.letter_menu_contents).getLayoutParams().width =  findViewById(R.id.letter_menu_container).getWidth();
            }
            isScaled = true;
        }
    }

    public void onLetterSelect(View view){
        Button b = (Button)view;
        presenter.setSelectedLetter(b.getText().charAt(0));

        sharedPrefs = this.getSharedPreferences(REPOSITORY, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(GAME_TYPE, BASIC_GAME);
        editor.apply();

        Intent intent = new Intent(this, LetterGameActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void loadTest(){
        sharedPrefs = this.getSharedPreferences(REPOSITORY, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(GAME_TYPE, TEST_GAME);
        editor.apply();

        Intent intent = new Intent(this, LetterGameActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 2:
                presenter.onBasicGameResult();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void openUserData(){
        Intent intent = new Intent(this, StudentDataActivity.class);
        startActivityForResult(intent, 0);
    }

    public void openStudentProfile(){
        Intent intent = new Intent(this, StudentProfileActivity.class);
        startActivityForResult(intent, 0);
    }

    public void openMainMenu(){
        Intent intent = new Intent(this, StudentProfileActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_data:
                openUserData();
                return true;

            case R.id.action_profile:
                openStudentProfile();
                return true;

            case R.id.action_home:
                openMainMenu();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        presenter.onDestroy();
    }
}
