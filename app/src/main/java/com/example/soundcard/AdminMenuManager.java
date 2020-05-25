package com.example.soundcard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminMenuManager extends AppCompatActivity {

    private final static String USER_LIST = "USER_LIST";
    private final static String USER_NAMES = "USER_NAMES";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private ArrayList<String> userList = new ArrayList<>();

    public AdminMenuManager(Context context){
        Log.d("LOADING ADMIN MANAGER", "...");
        loadUserList(context);
    }

    private void loadUserList(Context context){
        Log.d("TESTING", "in load user list");
        sharedPrefs = context.getSharedPreferences(USER_NAMES, Context.MODE_PRIVATE);
        String s = sharedPrefs.getString(USER_LIST, null);
        String[] names;
        if(s != null) {
            names = s.split(":");
            for (String name : names) {
                userList.add(name);
            }
        }
        Log.d("TESTING", "userlist: " + userList);
    }

    void saveNewUser(Context context, String name){
        Log.d("TESTING", "in save user list");
        if(!userList.contains(name)) {
            userList.add(name);
            updateUserList(context, userList);
        }
        else{
            Log.d("CANNOT SAVE REPEAT USER", "name: " + name);
        }
    }

    void deleteUser(Context context, String name){
        //update userlist
        if(userList.contains(name)){
            userList.remove(name);
        }
        updateUserList(context, userList);

        //clear specific user data
        sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        editor.clear();
        editor.apply();
    }

    private String convertUserList(ArrayList<String> uList){
        String names = "";
        for (int i = 0; i < uList.size(); i++) {
            names += uList.get(i);
            if (i != uList.size() - 1) {
                names += ":";
            }
        }
        return names;
    }

    private void updateUserList(Context context, ArrayList<String> newUserList){
        sharedPrefs = context.getSharedPreferences(USER_NAMES, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        editor.putString(USER_LIST, convertUserList(newUserList));
        editor.apply();
    }

    public List<String> getUserList(){return userList;}

}
