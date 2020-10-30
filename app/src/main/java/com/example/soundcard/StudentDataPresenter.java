package com.example.soundcard;

import java.util.ArrayList;

public class StudentDataPresenter implements StudentDataContract.Presenter{

    private ArrayList<String> testResultHistory = new ArrayList<>();

    private StudentDataContract.View view;

    public StudentDataPresenter(StudentDataContract.View view, String dirtyText){
        this.view = view;

        if(dirtyText != null){
            loadTestResults(dirtyText);
        }
    }

    public String getUserLvlText(int[] lvls){
        int lvl = getUserLevel(lvls);
        if(lvl == 0){
            return "Advance user past UPPERCASE";
        }
        else if(lvl == 1){
            return "Advance user past LOWERCASE";
        }
        return "idk i need code here";
    }

    public int getUserLevel(int[] lvls){
        int lvl = 0;
        for(int i: lvls){
            if(i == 1){
                lvl++;
            }
        }
        return lvl;
    }

    private void loadTestResults(String text){
        String dirtyAns = text;
        String[] ansHolder = {"","",""};
        String answer = "";
        int pos = 0;
        Boolean cutOutTime = true;
        Boolean isBothCases = false;
        for(int i = 0; i<dirtyAns.length(); i++){
            if(cutOutTime){
                ansHolder[pos] = dirtyAns.substring(i,i+12);
                pos++;
                i+=11;
                cutOutTime = false;
            }
            else{
                char letter = dirtyAns.charAt(i);
                if(letter == '^'){
                    isBothCases = true;
                }
                else if(letter == '&'){
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
                    isBothCases = false;
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

    public ArrayList<String> getTestResultHistory(){return testResultHistory;}
}
