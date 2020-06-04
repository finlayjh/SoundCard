package com.example.soundcard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class UserManager {

    private final static String USER_LETTERS = "USER_LETTERS";
    private final static String USER_DATA = "USER_DATA";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private String currentUser;
    private Case currentCase;
    private char currentLetter;
    private String userLowercaseLetters;
    private String userUppercaseLetters;
    private String userBothCaseLetters;


    protected UserManager(Context context, String userName){
        Log.d("LOADING USERMANAGER", "...");
        currentUser = userName;
        sharedPrefs = context.getSharedPreferences(currentUser, Context.MODE_PRIVATE);
        loadUser();
    }

    private void loadUser(){
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

    private void saveUser(){
        editor = sharedPrefs.edit();

        //formatting with :
        String userLetters = userUppercaseLetters +":"+ userLowercaseLetters +":"+ userBothCaseLetters;

        editor.putString(USER_LETTERS, userLetters);
        editor.apply();
    }


    //temporary?
    protected void clearUser(){
        editor = sharedPrefs.edit();
        if(currentCase == Case.UPPERCASE){
            userUppercaseLetters = "";
        }
        else if(currentCase == Case.LOWERCASE){
            userLowercaseLetters = "";
        }
        else if(currentCase == Case.BOTH){
            userBothCaseLetters = "";
        }
        saveUser();
    }

    protected void addUserLetter(char letter){
        Log.d("TESTING", "in adduserletters");
        if(currentCase == Case.UPPERCASE){
            if(!userUppercaseLetters.contains(String.valueOf(letter))){
                userUppercaseLetters += letter;
            }
        }
        else if(currentCase == Case.LOWERCASE){
            if(!userLowercaseLetters.contains(String.valueOf(letter))){
                userLowercaseLetters += letter;
            }
        }
        else if(currentCase == Case.BOTH){
            letter = Character.toUpperCase(letter);
            if(!userBothCaseLetters.contains(String.valueOf(letter))){
                userBothCaseLetters += letter;
            }
        }
        saveUser();
    }

    protected void saveTestResults(String rightAns, String wrongAns){
        editor = sharedPrefs.edit();

        String oldData = sharedPrefs.getString(USER_DATA, null);
        String sdf = new SimpleDateFormat("MMddyyyyHHmm", Locale.getDefault()).format(new Date());
        String newData = rightAns.toUpperCase() + "&" + wrongAns.toUpperCase();

        String finalData = "";
        finalData += sdf;
        if(currentCase == Case.BOTH){
            finalData += "^";
        }
        finalData += newData;
        if(oldData != null){
            finalData += ":" + oldData;
        }

        editor.putString(USER_DATA, finalData);
        editor.apply();
    }

    protected String getUserLetters(){
        if(currentCase == Case.UPPERCASE){
            return userUppercaseLetters;
        }
        else if(currentCase == Case.LOWERCASE){
            return userLowercaseLetters;
        }
        else if(currentCase == Case.BOTH){
            return userBothCaseLetters;
        }
        return null;
    }

    protected String getCurrentUser(){
        return currentUser;
    }

    protected void setCurrentCase(Case mCase){
        currentCase = mCase;
    }

    protected Case getCurrentCase(){
        return currentCase;
    }

    protected char getCurrentLetter(){return currentLetter;}

    protected void setCurrentLetter(char c){currentLetter =c;}
}
