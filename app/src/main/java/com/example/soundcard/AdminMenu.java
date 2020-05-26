package com.example.soundcard;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NewUserDialog.NewUserDialogListener, UserDeleteDialog.DeleteUserDialogListener {



    static AdminMenuManager adminMenuManager;

    private Spinner userSelectSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO get rid of letters showing on screen
        //TODO lock advanced stuff
        //TODO override to advanced stuff
        //TODO better history
        //TODO fix popup
        //TODO 3 buttons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        adminMenuManager = new AdminMenuManager(this);

        userSelectSpinner = findViewById(R.id.USER_SPINNER);
        userSelectSpinner.setOnItemSelectedListener(this);
        loadUserSpinner();
    }

    public void openNewUserDialog(View v){
        NewUserDialog newUserDialog = new NewUserDialog();
        newUserDialog.show(getSupportFragmentManager(), "new user dialog");
    }

    @Override
    public void saveNewUser(String name) {
        adminMenuManager.saveNewUser(this, name);
        loadUserSpinner();
    }

    public void deleteUserDialog(View v){
        UserDeleteDialog userDeleteDialog = new UserDeleteDialog(userSelectSpinner.getSelectedItem().toString());
        userDeleteDialog.show(getSupportFragmentManager(), "delete user dialog");
    }

    public void openUserData(View v){
        Intent intent = new Intent(v.getContext(), DataMenu.class);
        intent.putExtra("current_user", userSelectSpinner.getSelectedItem().toString());
        startActivityForResult(intent, 0);
    }

    public void loadUserSpinner(){
        // Spinner Drop down elements
        List<String> categories = adminMenuManager.getUserList();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        userSelectSpinner.setAdapter(dataAdapter);
    }

    public void deleteUser(Boolean confirm){
        if(confirm){
            adminMenuManager.deleteUser(this, userSelectSpinner.getSelectedItem().toString());
            loadUserSpinner();
        }
    }

    public void loadGame(View v){
        String name = userSelectSpinner.getSelectedItem().toString();
        if(!name.equals("")){
            Intent intent = new Intent(this, GameSelectActivity.class);
            intent.putExtra("current_user", name);
            startActivityForResult(intent, 0);
        }
        else{
            SimplePopUpMessage simplePopUpMessage = new SimplePopUpMessage("Please select or add a new user.");
            simplePopUpMessage.show(getSupportFragmentManager(), "simple message");
        }
    }


    //spinner overrides
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
