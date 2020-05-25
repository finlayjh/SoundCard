package com.example.soundcard;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class GameManager{

    private Random r = new Random();

    private int consecutiveWins;
    private char currentLetter;
    private boolean isFirstTry = true;
    private int currentButtonSlot;
    private String remainingTestLetters;
    private String correctTestAnswers;
    private String wrongTestAnswers;

    private static GameManagerInterface gameManagerInterface;

    public GameManager(GameManagerInterface gci){
        gameManagerInterface = gci;
    }

    public void loadBasicGame(){
        gameManagerInterface.updateCounter(consecutiveWins=0);
        gameManagerInterface.showLetter(currentLetter);
        newRound();
    }

    public void loadTest(String uLetters){
        String tempList = uLetters;
        String maxTenLetters = "";
        if(tempList.length()>10) {
            for (int i = 0; i < 10; i++) {
                char c = tempList.charAt(r.nextInt(tempList.length()));
                maxTenLetters += c;
                tempList = tempList.replace(Character.toString(c), "");
            }
            remainingTestLetters = maxTenLetters;
        }
        else{
            remainingTestLetters = uLetters;
        }
        setCurrentLetter(remainingTestLetters.charAt(r.nextInt(remainingTestLetters.length())));
        correctTestAnswers = "";
        wrongTestAnswers = "";
        newRound();
    }

    private void newRound(){
        Log.d("TESTING", "newround");
        Log.d("TESTING", "current letter: " + currentLetter);
        char[] newChars = new char[3];
        Case c = gameManagerInterface.getCurrentCase();
        if(gameManagerInterface.getCurrentCase() == Case.BOTH){
            int coinFlip = r.nextInt(2);
            currentLetter = (char)(Character.toUpperCase(currentLetter)+32*coinFlip);
        }

        // main question screen
        currentButtonSlot = genNewButtonSlot();
        char otherUsedChar = '\0';
        for (int i = 0; i < 3; i++) {
            if (i == currentButtonSlot) {
                newChars[i] = currentLetter;
            } else if(otherUsedChar == '\0'){
                otherUsedChar  = genUniqueChar(currentLetter);
                newChars[i] = otherUsedChar;
            } else{
                newChars[i] = genUniqueChar(currentLetter, otherUsedChar);
            }
        }
        gameManagerInterface.updateButtons(newChars[0],newChars[1],newChars[2]);
    }

    public boolean checkAnswer(char answer){
        Log.d("TESTING", "button: " + answer);
        //basic game
        if(remainingTestLetters == null) {
            if (answer == currentLetter) {
                if (isFirstTry) {
                    isFirstTry = true;
                    if (gameManagerInterface.updateCounter(++consecutiveWins)) {
                        newRound();
                    }
                    return true;
                } else {
                    isFirstTry = true;
                    newRound();
                    return true;
                }
            } else {
                gameManagerInterface.updateCounter(consecutiveWins = 0);
                isFirstTry = false;
                return false;
            }
        }
        //review/test game
        else{
            if(answer == currentLetter && (!correctTestAnswers.contains(String.valueOf(Character.toLowerCase(answer))) && !correctTestAnswers.contains(String.valueOf(Character.toUpperCase(answer))))){
                correctTestAnswers += currentLetter;
                newTestRoundPaperwork();
                return true;
            }
            else{
                if(!wrongTestAnswers.contains(String.valueOf(Character.toLowerCase(answer))) && !correctTestAnswers.contains(String.valueOf(Character.toUpperCase(answer)))){
                    wrongTestAnswers += currentLetter;

                }
                newTestRoundPaperwork();
                return false;
            }
        }
    }

    public void newTestRoundPaperwork(){
        Log.d("TESTING", "in newtestpprwrk");
        remainingTestLetters = remainingTestLetters.replace(String.valueOf(Character.toUpperCase(currentLetter)), "");
        remainingTestLetters = remainingTestLetters.replace(String.valueOf(Character.toLowerCase(currentLetter)), "");
        Log.d("TESTING", "remainingTestLetters: " + remainingTestLetters);
        Log.d("TESTING", "correctAnswers: " + correctTestAnswers);
        Log.d("TESTING", "wrongAnswers: " + wrongTestAnswers);
        if(remainingTestLetters == ""){
            Log.d("TESTING", "ending test..");
            remainingTestLetters = null;
            gameManagerInterface.finishTest(correctTestAnswers, wrongTestAnswers);
        }
        else{
            setCurrentLetter(remainingTestLetters.charAt(r.nextInt(remainingTestLetters.length())));
            newRound();
        }
    }

    // c are chars already in use
    private char genUniqueChar(char ... c){
        Log.d("TESTING", "gen unique char");
        char newChar = (char)(r.nextInt(26) + 'A');
        for(char i: c){
            if(newChar == i){
                return genUniqueChar(c);
            }
        }
        if(gameManagerInterface.getCurrentCase() == Case.LOWERCASE){
            return (char)(newChar+32);
        }
        else if(gameManagerInterface.getCurrentCase() == Case.BOTH){
            int coinFlip = r.nextInt(2);
            newChar += 32*coinFlip;
            for(char i: c){
                if(i == Character.toUpperCase(newChar) || i == Character.toLowerCase(newChar)){
                    return genUniqueChar(c);
                }
            }
            return newChar;
        }
        return newChar;
    }

    // need a different slot for answer so kids don't learn letter based of location
    private int genNewButtonSlot(){
        int tempNum = r.nextInt(3);
        if(tempNum == currentButtonSlot){
            return genNewButtonSlot();
        }
        return tempNum;
    }

    public char getCurrentLetter() {
        return currentLetter;
    }
    public void setCurrentLetter(char c){
        currentLetter = c;
        gameManagerInterface.setCurrentLetter(c);
    }

    public interface GameManagerInterface {
        void setCurrentLetter(char c);
        void showLetter(char c);
        void updateButtons(char nText1, char nText2, char nText3);
        boolean updateCounter(int count);
        void finishTest(String correctAns, String wrongAns);
        Case getCurrentCase();
    }
}