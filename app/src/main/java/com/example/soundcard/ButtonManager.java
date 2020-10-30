package com.example.soundcard;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

enum Case{
    UPPERCASE, LOWERCASE, BOTH
}

public class ButtonManager {

    private PorterDuffColorFilter redFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
    private PorterDuffColorFilter greenFilter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

    private Map<String, Button> buttonMap;
    private Case currentCase = Case.UPPERCASE;

    public ButtonManager(View view){
        //this is probably wrong way
        buttonMap = new HashMap<>();
        buttonMap.put("A", (Button)view.findViewById(R.id.btn_A));
        buttonMap.put("B", (Button)view.findViewById(R.id.btn_B));
        buttonMap.put("C", (Button)view.findViewById(R.id.btn_C));
        buttonMap.put("D", (Button)view.findViewById(R.id.btn_D));
        buttonMap.put("E", (Button)view.findViewById(R.id.btn_E));
        buttonMap.put("F", (Button)view.findViewById(R.id.btn_F));
        buttonMap.put("G", (Button)view.findViewById(R.id.btn_G));
        buttonMap.put("H", (Button)view.findViewById(R.id.btn_H));
        buttonMap.put("I", (Button)view.findViewById(R.id.btn_I));
        buttonMap.put("J", (Button)view.findViewById(R.id.btn_J));
        buttonMap.put("K", (Button)view.findViewById(R.id.btn_K));
        buttonMap.put("L", (Button)view.findViewById(R.id.btn_L));
        buttonMap.put("M", (Button)view.findViewById(R.id.btn_M));
        buttonMap.put("N", (Button)view.findViewById(R.id.btn_N));
        buttonMap.put("O", (Button)view.findViewById(R.id.btn_O));
        buttonMap.put("P", (Button)view.findViewById(R.id.btn_P));
        buttonMap.put("Q", (Button)view.findViewById(R.id.btn_Q));
        buttonMap.put("R", (Button)view.findViewById(R.id.btn_R));
        buttonMap.put("S", (Button)view.findViewById(R.id.btn_S));
        buttonMap.put("T", (Button)view.findViewById(R.id.btn_T));
        buttonMap.put("U", (Button)view.findViewById(R.id.btn_U));
        buttonMap.put("V", (Button)view.findViewById(R.id.btn_V));
        buttonMap.put("W", (Button)view.findViewById(R.id.btn_W));
        buttonMap.put("X", (Button)view.findViewById(R.id.btn_X));
        buttonMap.put("Y", (Button)view.findViewById(R.id.btn_Y));
        buttonMap.put("Z", (Button)view.findViewById(R.id.btn_Z));
    }

    //also used to recolor single buttons
    @TargetApi(16)
    public void loadUserButtons(String userLetters){
        Log.d("TESTING", "in loaduserbuttons");
        Log.d("its ya boi", "userletters: " + userLetters);
        if(userLetters!="") {
            int offset = 0;
            if(Character.isLowerCase(userLetters.charAt(0))){offset = -32;}
            for (int i = 0; i < userLetters.length(); i++) {
                Button b = buttonMap.get(Character.toString((char)(userLetters.charAt(i) + offset)));
                b.setBackgroundResource(R.drawable.letter_menu_completed_btn);
            }
        }
    }

    //pass null to clear all
    public void resetButtons(String userLetters){
        Log.d("TESTING", "butts: " + userLetters);
        if(userLetters == null){
            for(Button b: buttonMap.values()){
                b.getBackground().clearColorFilter();
            }
        }
        else{
            for(int i = 0; i<userLetters.length(); i++){
                Button b = buttonMap.get(Character.toString(Character.toUpperCase(userLetters.charAt(i))));
                b.getBackground().clearColorFilter();
            }
        }
    }

    public void swapToCase(Case myCase, String newUserLetters) {
        Log.d("TESTING", "butts: " + newUserLetters);

        switch(myCase){
            case UPPERCASE:
                if(currentCase != Case.UPPERCASE){
                    currentCase = Case.UPPERCASE;
                    for (Map.Entry<String, Button> entry : buttonMap.entrySet()) {
                            entry.getValue().setText(entry.getKey());
                            entry.getValue().setAllCaps(false);
                    }
                }
                break;
            case LOWERCASE:
                if(currentCase != Case.LOWERCASE){
                    currentCase = Case.LOWERCASE;
                    for (Map.Entry<String, Button> entry : buttonMap.entrySet()) {
                        entry.getValue().setText(entry.getKey().toLowerCase());
                        entry.getValue().setAllCaps(false);
                    }
                }
                break;
            case BOTH:
                if(currentCase != Case.BOTH){
                    currentCase = Case.BOTH;
                    for (Map.Entry<String, Button> entry : buttonMap.entrySet()) {
                        entry.getValue().setText(entry.getKey() + entry.getKey().toLowerCase());
                        entry.getValue().setAllCaps(false);
                    }
                }
                break;
            default:
                Log.d("ERROR", "not passing correct CASE to swaptocase");
        }
        resetButtons(null);
        loadUserButtons(newUserLetters);
    }
}
