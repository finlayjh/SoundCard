package com.example.soundcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StudentProfileActivity extends AppCompatActivity implements StudentProfileContract.View, StudentDeleteDialog.DeleteUserDialogListener, NewStudentDialog.NewUserDialogListener {

    private final static String REPOSITORY = "REPOSITORY";
    private final static String CURRENT_STUDENT = "CURRENT_STUDENT";
    private final static String STUDENT_PROFILE = "STUDENT_PROFILE";
    private final static String STUDENT_LIST = "STUDENT_LIST";
    private final static String USER_INFO = "USER_INFO";

    StudentProfilePresenter presenter;

    private SharedPreferences sharedPrefs;
    private LayoutScalerUtility layoutScalerUtility;
    private boolean isScaled = false;
    private TextView tvName;
    private TextView tvTeacher;
    private TextView tvAge;
    private TextView tvGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Toolbar myToolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profile");

        presenter = new StudentProfilePresenter(this);

        //scale layout
        layoutScalerUtility = new LayoutScalerUtility();

        //load profile
        tvName = findViewById(R.id.tv_name);
        tvTeacher = findViewById(R.id.tv_teacher);
        tvGrade = findViewById(R.id.tv_grade);
        tvAge = findViewById(R.id.tv_age);
        loadProfile();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(!isScaled) {
            layoutScalerUtility.scaleContents(findViewById(R.id.student_profile_contents), findViewById(R.id.student_profile_container));
            if(findViewById(R.id.student_profile_contents).getLayoutParams().width !=  findViewById(R.id.student_profile_container).getWidth()){
                findViewById(R.id.student_profile_contents).getLayoutParams().width =  findViewById(R.id.student_profile_container).getWidth();
            }
            isScaled = true;
        }
    }

    private void loadProfile(){
        sharedPrefs = this.getSharedPreferences(REPOSITORY, this.MODE_PRIVATE);
        String name = sharedPrefs.getString(CURRENT_STUDENT, null);
        sharedPrefs = this.getSharedPreferences(name, this.MODE_PRIVATE);
        String profileInfo = sharedPrefs.getString(STUDENT_PROFILE, null);
        presenter.loadProfile(profileInfo);
        tvName.setText(presenter.getStudentProfile().getFirstName()+"\n"+presenter.getStudentProfile().getLastName());
        tvTeacher.setText("Teacher: " + presenter.getStudentProfile().getTeacher());
        tvGrade.setText("Grade: ");
        tvAge.setText("Age: " + presenter.getStudentProfile().getAge());
    }

    public void openUserData(){
        Intent intent = new Intent(this, StudentDataActivity.class);
        startActivityForResult(intent, 0);
    }

    public void openMainMenu(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivityForResult(intent, 0);
    }

    public void deleteStudentDialog(View v){
        StudentDeleteDialog studentDeleteDialog = new StudentDeleteDialog(presenter.getStudentProfile().getFirstName()+" "+presenter.getStudentProfile().getLastName());
        studentDeleteDialog.show(getSupportFragmentManager(), "delete user dialog");
    }

    public void editStudentDialog(View v){
        NewStudentDialog newStudentDialog = new NewStudentDialog(presenter.getStudentProfile());
        newStudentDialog.show(getSupportFragmentManager(), "delete user dialog");
    }

    public void deleteUser(String name){
        sharedPrefs = this.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();

        sharedPrefs = this.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String names = sharedPrefs.getString(STUDENT_LIST, null);
        editor = sharedPrefs.edit();
        editor.putString(STUDENT_LIST, presenter.deleteStudent(names));
        editor.apply();

        openMainMenu();
    }

    @Override
    public void saveNewStudent(String fName, String lName, String teacher, String grade, String age) {
        Student editedStudent = new Student(fName, lName, teacher, grade, age);

        //delete old profile
        String oldName = presenter.getStudentProfile().getFirstName()+"+"+presenter.getStudentProfile().getLastName();
        sharedPrefs = this.getSharedPreferences(oldName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();

        //save new student list
        sharedPrefs = this.getSharedPreferences(USER_INFO, this.MODE_PRIVATE);
        String newList = presenter.editStudent(editedStudent, sharedPrefs.getString(STUDENT_LIST, null));
        editor = sharedPrefs.edit();
        editor.putString(STUDENT_LIST, newList);
        editor.apply();

        //save new profile
        String newName = presenter.getStudentProfile().getFirstName()+"+"+presenter.getStudentProfile().getLastName();
        sharedPrefs = this.getSharedPreferences(newName, this.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.putString(STUDENT_PROFILE, presenter.getStudentProfile().getPackagedStudent());
        editor.apply();

        //update profile
        tvName.setText(presenter.getStudentProfile().getFirstName()+"\n"+presenter.getStudentProfile().getLastName());
        tvTeacher.setText("Teacher: " + presenter.getStudentProfile().getTeacher());
        tvGrade.setText("Grade: ");
        tvAge.setText("Age: " + presenter.getStudentProfile().getAge());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_profile);
        item.setVisible(false);
        return true;
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
