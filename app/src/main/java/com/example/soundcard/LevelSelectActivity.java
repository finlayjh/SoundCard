package com.example.soundcard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class LevelSelectActivity extends AppCompatActivity implements LevelSelectContract.View {

    private final static String USER_INFO = "USER_INFO";
    private final static String REPOSITORY = "REPOSITORY";
    private final static String CURRENT_STUDENT = "CURRENT_STUDENT";

    private TextView tvName;
    private Button button;
    private LevelSelectPresenter presenter;
    private SharedPreferences sharedPrefs;
    private LayoutScalerUtility layoutScalerUtility;
    private boolean isScaled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        sharedPrefs = this.getSharedPreferences(REPOSITORY, this.MODE_PRIVATE);
        String name = sharedPrefs.getString(CURRENT_STUDENT, null);
        tvName = findViewById(R.id.tv_username);
        tvName.setText(name);
        UserManager userManager = new UserManager(this, name);
        layoutScalerUtility = new LayoutScalerUtility();

        //toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Level Select");

        presenter = new LevelSelectPresenter(this, userManager);

        sharedPrefs = this.getSharedPreferences(name, Context.MODE_PRIVATE);
        loadButtons(userManager.getLessonsCompleted());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(!isScaled) {
            layoutScalerUtility.scaleContents(findViewById(R.id.level_select_contents), findViewById(R.id.level_select_container));
            if(findViewById(R.id.level_select_contents).getLayoutParams().width !=  findViewById(R.id.level_select_container).getWidth()){
                findViewById(R.id.level_select_contents).getLayoutParams().width =  findViewById(R.id.level_select_container).getWidth();
            }
            isScaled = true;
        }
    }

    @TargetApi(21)
    private void loadButtons(int[] completedLessons){
        Log.d("testing", "uqhsgdfjhgd: " + completedLessons[0] + completedLessons[1] + completedLessons[2]);
        for(int i = 0; i<3; i++){
            if(i == 0){
                button = findViewById(R.id.btn_ABC);
                if(completedLessons[i] == 1){
                    //button.getBackground().getCurrent().setTint(getResources().getColor(R.color.defaultGreen));
                }
            }
            else if(i == 1){
                button = findViewById(R.id.btn_abc);
                if(completedLessons[i] == 1 && completedLessons[0] == 1){
                    //button.getBackground().getCurrent().setTint(getResources().getColor(R.color.defaultGreen));
                }
                if(completedLessons[0] == 0){
                    button.setEnabled(false);
                }
            }
            else if(i == 2){
                button = findViewById(R.id.btn_AaBbCc);
                if(completedLessons[i] == 1 && completedLessons[1] == 1 && completedLessons[0] == 1){
                    //button.getBackground().getCurrent().setTint(getResources().getColor(R.color.defaultGreen));
                }
                if(completedLessons[1] == 0 || completedLessons[0] == 0){
                    button.setEnabled(false);
                }
            }
        }
    }

    public void loadLetterSelect(View view){
        Button b = (Button)view;
        presenter.setUserCase(b.getText().toString());
        Intent intent = new Intent(this, LetterMenuActivity.class);
        startActivityForResult(intent, 0);
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
}