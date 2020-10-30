package com.example.soundcard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class StudentDataActivity extends AppCompatActivity implements StudentDataContract.View, UserManagerDialog.UserManagerDialogListener {

    private final static String USER_DATA = "USER_DATA";
    private final static String REPOSITORY = "REPOSITORY";
    private final static String CURRENT_STUDENT = "CURRENT_STUDENT";

    StudentDataContract.Presenter presenter;

    private TextView pageTitle;
    private Button btnOpenUsermanager;
    private UserManager userManager;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LOADING DATA MENU", "...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);

        sharedPrefs = this.getSharedPreferences(REPOSITORY, this.MODE_PRIVATE);
        String currentUser = sharedPrefs.getString(CURRENT_STUDENT, null);

        sharedPrefs = this.getSharedPreferences(currentUser, this.MODE_PRIVATE);
        String text = sharedPrefs.getString(USER_DATA, null);

        //toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Data");

        //load username to page title
        pageTitle = findViewById(R.id.PAGE_TITLE);
        pageTitle.setText(currentUser);
        btnOpenUsermanager = findViewById(R.id.btn_user_manager);
        userManager = new UserManager(this, currentUser);

        presenter = new StudentDataPresenter(this, text);

        if(text != null) {

            //display saved answers
            LinearLayout linearLayout = findViewById(R.id.SCROLL_VIEW);
            ArrayList<String> history = presenter.getTestResultHistory();
            for (int i = 0; i < history.size(); i++) {
                TextView TV = new TextView(this);
                TV.setText(history.get(i));
                TV.setId(i);
                TV.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(TV);
            }
        }

        btnOpenUsermanager.setText("Manage " + userManager.getCurrentUser() + "'s Progress");
    }

    public void openUserManagerDialog(View v){
        UserManagerDialog userManagerDialog = new UserManagerDialog(userManager.getAllNumLetters());
        userManagerDialog.show(getSupportFragmentManager(), "user manager dialog");
    }

    public void updateUser(int[] newInfo){
        for(int i = 0; i<newInfo.length; i++){
            if(newInfo[i] != -1){
                if(i == 0){
                    if(newInfo[i] == 0){
                        userManager.clearUser(Case.UPPERCASE);
                    }
                    else{
                        userManager.advanceUser(1);
                    }
                }
                if(i == 1){
                    if(newInfo[i] == 0){
                        userManager.clearUser(Case.LOWERCASE);
                    }
                    else{
                        userManager.advanceUser(2);
                    }
                }
                if(i == 2){
                    if(newInfo[i] == 0){
                        userManager.clearUser(Case.BOTH);
                    }
                }
            }
        }
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
