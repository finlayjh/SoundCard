package com.example.soundcard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UserManager {

    private final static String USER_LETTERS = "USER_LETTERS";
    private final static String USER_DATA = "USER_DATA";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private String currentUser;
    private String userLowercaseLetters;
    private String userUppercaseLetters;
    private String userBothCaseLetters;


    public UserManager(Context context, String userName){
        Log.d("LOADING USERMANAGER", "...");
        currentUser = userName;
        loadUser(context);
    }

    private void loadUser(Context context){
        sharedPrefs = context.getSharedPreferences(currentUser, Context.MODE_PRIVATE);

        String allLetters = sharedPrefs.getString(USER_LETTERS, null);
        userLowercaseLetters = "";
        userUppercaseLetters = "";
        userBothCaseLetters = "";

        if(allLetters != null) {
            int colonCounter = 0;
            for (int i = 0; i < allLetters.length(); i++) {
                char c = allLetters.charAt(i);
                if (c == ':') {
                    colonCounter++;
                    continue;
                }
                switch (colonCounter) {
                    case 0:
                        userUppercaseLetters += c;
                        break;
                    case 1:
                        userLowercaseLetters += c;
                        break;
                    case 2:
                        userBothCaseLetters += c;
                        break;
                }
            }
        }
    }

    private void saveUser(Context context){
        sharedPrefs = context.getSharedPreferences(currentUser, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        //formatting with :
        String userLetters = userUppercaseLetters +":"+ userLowercaseLetters +":"+ userBothCaseLetters;

        editor.putString(USER_LETTERS, userLetters);
        editor.apply();
    }


    //temporary?
    public void clearUser(Context context, Case myCase){
        sharedPrefs = context.getSharedPreferences(currentUser, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        if(myCase == Case.UPPERCASE){
            userUppercaseLetters = "";
        }
        else if(myCase == Case.LOWERCASE){
            userLowercaseLetters = "";
        }
        else if(myCase == Case.BOTH){
            userBothCaseLetters = "";
        }
        saveUser(context);
    }

    public void addUserLetter( Context context, char letter, Case myCase){
        Log.d("TESTING", "in adduserletters");
        if(myCase == Case.UPPERCASE){
            if(!userUppercaseLetters.contains(String.valueOf(letter))){
                userUppercaseLetters += letter;
            }
        }
        else if(myCase == Case.LOWERCASE){
            if(!userLowercaseLetters.contains(String.valueOf(letter))){
                userLowercaseLetters += letter;
            }
        }
        else if(myCase == Case.BOTH){
            letter = Character.toUpperCase(letter);
            if(!userBothCaseLetters.contains(String.valueOf(letter))){
                userBothCaseLetters += letter;
            }
        }
        saveUser(context);
    }

    public void saveTestResults(Context context,String rightAns, String wrongAns, Boolean isBothCase){
        sharedPrefs = context.getSharedPreferences(currentUser, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        String oldData = sharedPrefs.getString(USER_DATA, null);
        String sdf = new SimpleDateFormat("MMddyyyyHHmm", Locale.getDefault()).format(new Date());
        String newData = sdf + rightAns.toUpperCase() + "&" + wrongAns.toUpperCase();

        String finalData = "";
        if(isBothCase){
            finalData += "^";
        }
        finalData += newData;
        if(oldData != null){
            finalData += ":" + oldData.substring(1);
        }

        editor.putString(USER_DATA, finalData);
        editor.apply();
    }

    public String getUserLetters(Case myCase){
        if(myCase == Case.UPPERCASE){
            return userUppercaseLetters;
        }
        else if(myCase == Case.LOWERCASE){
            return userLowercaseLetters;
        }
        else if(myCase == Case.BOTH){
            return userBothCaseLetters;
        }
        return null;
    }
}
