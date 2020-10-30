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
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity implements MainMenuContract.View, AdapterView.OnItemSelectedListener, NewStudentDialog.NewUserDialogListener, DialogConfirmAdvance.DialogConfirmAdvanceListener{

    private final static String STUDENT_LIST = "STUDENT_LIST";
    private final static String USER_INFO = "USER_INFO";
    private final static String STUDENT_PROFILE = "STUDENT_PROFILE";
    private final static String REPOSITORY = "REPOSITORY";
    private final static String CURRENT_STUDENT = "CURRENT_STUDENT";

    MainMenuPresenter presenter;

    private Spinner studentSelectSpinner;
    private Spinner filterSelectSpinner;
    private SharedPreferences sharedPrefs;
    private boolean isScaled = false;
    private LayoutScalerUtility layoutScalerUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //small
        //big
        //todo add settings window
        //todo rework data page
        //art
        //todo icons for delete/edit on profile
        //todo icons for navigation
        //todo wincounter to image
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar myToolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MainMenu");

        presenter = new MainMenuPresenter(this);

        //scale layout
        layoutScalerUtility = new LayoutScalerUtility();

        //load spinners
        studentSelectSpinner = findViewById(R.id.spinner_student);
        studentSelectSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                {
                    if(parent.getItemAtPosition(position).toString().contains("-")){
                        parent.setSelection(position+1);
                        setCurrentStudent();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sharedPrefs = this.getSharedPreferences(USER_INFO, this.MODE_PRIVATE);
        String[] names = presenter.loadNameList(sharedPrefs.getString(STUDENT_LIST, null));
        if(names != null){
            for(String name: names){
                sharedPrefs = this.getSharedPreferences(name, this.MODE_PRIVATE);
                presenter.loadStudent(sharedPrefs.getString(STUDENT_PROFILE, null));
            }
        }
        loadSpinner(studentSelectSpinner);
        filterSelectSpinner = findViewById(R.id.spinner_filter);
        filterSelectSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        {
                            loadSpinner(studentSelectSpinner);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
        });
        loadSpinner(filterSelectSpinner);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(!isScaled) {
            layoutScalerUtility.scaleContents(findViewById(R.id.main_menu_contents), findViewById(R.id.main_menu_container));
            if(findViewById(R.id.main_menu_contents).getLayoutParams().width !=  findViewById(R.id.main_menu_container).getWidth()){
                findViewById(R.id.main_menu_contents).getLayoutParams().width =  findViewById(R.id.main_menu_container).getWidth();
            }
            isScaled = true;
        }
    }

    @Override
    public void saveNewStudent(String fName, String lName, String teacher, String grade, String age) {
        Student newStudent = new Student(fName, lName, teacher, grade, age);
        if(presenter.saveNewStudent(newStudent)) {
            loadSpinner(studentSelectSpinner);
            advanceUserDialog(newStudent.getFirstName());
            studentSelectSpinner.setSelection(((ArrayAdapter) studentSelectSpinner.getAdapter()).getPosition(newStudent.getFormatedName()));
        }
        else{
            Toast.makeText(this, "Repeat User", Toast.LENGTH_SHORT).show();
        }
    }

    public void advanceUser(int level, String name){
        UserManager userManager = new UserManager(this ,name);
        userManager.advanceUser(level);
    }

    public void updateStudentList(){
        sharedPrefs = this.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        Log.d("SAVING" , "STUDENT_LIST: " + presenter.packageStudentList());
        editor.putString(STUDENT_LIST, presenter.packageStudentList());
        editor.apply();
    }

    public void addStudentProfile(Student student){
        sharedPrefs = this.getSharedPreferences(student.getFirstName()+"+"+student.getLastName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        Log.d("SAVING" , "STUDENT_PROFILE: " + student.getPackagedStudent());
        editor.putString(STUDENT_PROFILE, student.getPackagedStudent());
        editor.apply();
    }

    public void loadSpinner(android.widget.Spinner spinner){
        if(spinner.getId() == R.id.spinner_student){
            List<String> categories;
            if(filterSelectSpinner == null){
                categories = presenter.getFilteredStudentList("Alphabetical");
            }
            else{
                categories = presenter.getFilteredStudentList(filterSelectSpinner.getSelectedItem().toString());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            studentSelectSpinner.setAdapter(dataAdapter);
        }
        else if(spinner.getId() == R.id.spinner_filter){
            List<String> categories = presenter.getFilterList();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filterSelectSpinner.setAdapter(dataAdapter);
        }
    }

    public void openNewUserDialog(View v){
        NewStudentDialog newStudentDialog = new NewStudentDialog(null);
        newStudentDialog.show(getSupportFragmentManager(), "new user dialog");
    }

    public void advanceUserDialog(String name){
        DialogConfirmAdvance dialogConfirmAdvance = new DialogConfirmAdvance(name);
        dialogConfirmAdvance.show(getSupportFragmentManager(), "advance user dialog");
    }

    public void loadGame(View v){
        String name = studentSelectSpinner.getSelectedItem().toString();
        if(isStudentSelected()) {
            Intent intent = new Intent(this, LevelSelectActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    private boolean isStudentSelected(){
        if(!studentSelectSpinner.getSelectedItem().toString().equals("<- Add your first user")){
            return true;
        }
        else{
            Toast.makeText(this, "Create a user", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setCurrentStudent(){
        sharedPrefs = this.getSharedPreferences(REPOSITORY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(CURRENT_STUDENT, presenter.getCurrentStudentName(studentSelectSpinner.getSelectedItem().toString()));
        editor.apply();
    }

    //spinner overrides
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_home);
        item.setVisible(false);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_data:
                if(isStudentSelected()) {
                    openUserData();
                }
                return true;

            case R.id.action_profile:
                if(isStudentSelected()) {
                    openStudentProfile();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
