package com.example.soundcard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DataMenu extends AppCompatActivity {

    private final static String USER_DATA = "USER_DATA";

    private String currentUser;
    private SharedPreferences sharedPrefs;
    private ArrayList<String> testResultHistory = new ArrayList<>();
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LOADING DATA MENU", "...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_menu);

        Bundle bundle = getIntent().getExtras();
        currentUser = bundle.getString("current_user");

        loadTestResults();

        //load username to page title
        pageTitle = findViewById(R.id.PAGE_TITLE);
        pageTitle.setText(currentUser);

        //display saved answers
        LinearLayout linearLayout = findViewById(R.id.SCROLL_VIEW);
        for(int i = 0; i<testResultHistory.size(); i++) {
            TextView TV = new TextView(this);
            TV.setText(testResultHistory.get(i));
            TV.setId(i);
            TV.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(TV);
        }
    }

    private void loadTestResults(){
        sharedPrefs = this.getSharedPreferences(currentUser, Context.MODE_PRIVATE);

        // loading test data
        String dirtyAns = sharedPrefs.getString(USER_DATA, null);
        String[] ansHolder = {"","",""};
        String answer = "";
        int pos = 0;
        Boolean cutOutTime = true;
        Boolean isBothCases = false;
        if(dirtyAns.charAt(0) == '^'){
            isBothCases = true;
            dirtyAns = dirtyAns.substring(1);
        }
        for(int i = 0; i<dirtyAns.length(); i++){
            if(cutOutTime){
                ansHolder[pos] = dirtyAns.substring(i,i+12);
                pos++;
                i+=11;
                cutOutTime = false;
            }
            else{
                char letter = dirtyAns.charAt(i);
                if(letter == '&'){
                    ansHolder[pos] = answer;
                    pos++;
                    answer = "";
                }
                else if(letter == ':'){
                    ansHolder[pos] = answer;
                    pos = 0;
                    answer = "";
                    testResultHistory.add(parseTestResults(ansHolder, isBothCases));
                    ansHolder = new String[]{"","",""};
                    cutOutTime = true;
                }
                else{
                    answer += letter;
                }
            }
        }
        ansHolder[pos] = answer;
        testResultHistory.add(parseTestResults(ansHolder, isBothCases));
    }

    private String parseTestResults(String[] data, Boolean isBoth){
        String readable = "";

        String d = data[0];
        readable += d.substring(0,2) + "/" + d.substring(2,4) + "/" + d.substring(4,8) + "\t";
        d = d.substring(8);
        int hour;
        if(d.charAt(0) == '0'){
            hour = Integer.parseInt(d.substring(1));
        }else{
            hour = Integer.parseInt(d.substring(0,2));
        }
        int minute = Integer.parseInt(d.substring(2));
        if(hour > 12){
            hour -= 12;
        }
        readable += hour + ":" + minute;
        String corr = "";
        String incorr = "";
        if(isBoth){
            for(char c:  data[1].toCharArray()){
                corr += String.valueOf(c) + String.valueOf(Character.toLowerCase(c));
            }
            for(char c:  data[2].toCharArray()){
                incorr += String.valueOf(c) + String.valueOf(Character.toLowerCase(c));
            }
        }
        else{
            corr = data[1];
            incorr = data[2];
        }
        readable += "\nCorrect: " + corr;
        readable += "\nIncorrect: " + incorr + "\n";

        return readable;
    }
}
