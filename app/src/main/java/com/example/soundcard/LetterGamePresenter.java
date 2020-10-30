package com.example.soundcard;

import java.util.Random;

public class LetterGamePresenter implements LetterGameContract.Presenter {

    SoundManager soundManager;
    UserManager userManager;
    private LetterGameContract.View view;

    private Random r = new Random();
    private int consecutiveWins;
    private int consecutiveLosses;
    private boolean isFirstTry = true;
    private int currentButtonSlot;
    private String remainingTestLetters;
    private String correctTestAnswers;
    private String wrongTestAnswers;

    public LetterGamePresenter(LetterGameContract.View view){

        soundManager = LetterMenuPresenter.soundManager;
        userManager = LetterMenuPresenter.userManager;
        this.view = view;

    }

    public void loadBasicGame(){
        view.updateWinCounter(consecutiveWins=0);
        consecutiveLosses =0;
        newRound();
    }

    public void loadTest(){
        view.updateWinCounter(-1);
        String userLetters = userManager.getUserLetters();
        String maxTenLetters = "";
        if(userLetters.length()>10) {
            for (int i = 0; i < 10; i++) {
                char c = userLetters.charAt(r.nextInt(userLetters.length()));
                maxTenLetters += c;
                userLetters = userLetters.replace(Character.toString(c), "");
            }
            remainingTestLetters = maxTenLetters;
        }
        else{
            remainingTestLetters = userLetters;
        }
        userManager.setCurrentLetter(remainingTestLetters.charAt(r.nextInt(remainingTestLetters.length())));
        soundManager.setCurrentLetter(userManager.getCurrentLetter());
        correctTestAnswers = "";
        wrongTestAnswers = "";
        newRound();
        //message
        view.openTestAlert();
    }

    private void newRound(){
        char[] newChars = new char[3];
        char c = userManager.getCurrentLetter();
        if(userManager.getCurrentCase() == Case.BOTH){
            int coinFlip = r.nextInt(2);
            c = (char)(userManager.getCurrentLetter()+32*coinFlip);
        }

        // main question screen
        currentButtonSlot = genNewButtonSlot();
        char otherUsedChar = '\0';
        for (int i = 0; i < 3; i++) {
            if (i == currentButtonSlot) {
                newChars[i] = c;
            } else if(otherUsedChar == '\0'){
                otherUsedChar  = genUniqueChar(userManager.getCurrentLetter());
                newChars[i] = otherUsedChar;
            } else{
                newChars[i] = genUniqueChar(userManager.getCurrentLetter(), otherUsedChar);
            }
        }
        view.updateButtons(newChars[0],newChars[1],newChars[2]);
    }

    public boolean checkAnswer(char answer){
        //basic game
        if(remainingTestLetters == null) {
            char c = userManager.getCurrentLetter();
            if (answer == Character.toUpperCase(userManager.getCurrentLetter()) || answer == Character.toLowerCase(userManager.getCurrentLetter())) {
                if (isFirstTry) {
                    consecutiveLosses = 0;
                    if (view.updateWinCounter(++consecutiveWins)) {
                        newRound();
                    }
                    return true;
                } else {
                    consecutiveLosses++;
                    isFirstTry = true;
                    newRound();
                    return true;
                }
            } else {
                consecutiveLosses++;
                if(consecutiveLosses == 3){
                    view.openTeachingModel(userManager.getCurrentLetter());
                }
                view.updateWinCounter(consecutiveWins = 0);
                isFirstTry = false;
                return false;
            }
        }
        //review/test game
        else{
            if(Character.toUpperCase(answer) == Character.toUpperCase(userManager.getCurrentLetter()) && (!correctTestAnswers.contains(String.valueOf(Character.toLowerCase(answer))) && !correctTestAnswers.contains(String.valueOf(Character.toUpperCase(answer))))){
                correctTestAnswers += userManager.getCurrentLetter();
                newTestRoundPaperwork();
                return true;
            }
            else{
                if(!wrongTestAnswers.contains(String.valueOf(Character.toLowerCase(answer))) && !correctTestAnswers.contains(String.valueOf(Character.toUpperCase(answer)))){
                    wrongTestAnswers += userManager.getCurrentLetter();
                }
                newTestRoundPaperwork();
                return false;
            }
        }
    }

    private void newTestRoundPaperwork(){
        if(userManager.getCurrentCase() == Case.BOTH){
            remainingTestLetters = remainingTestLetters.replace(String.valueOf(Character.toUpperCase(userManager.getCurrentLetter())), "");
            remainingTestLetters = remainingTestLetters.replace(String.valueOf(Character.toLowerCase(userManager.getCurrentLetter())), "");
        }
        else{
            remainingTestLetters = remainingTestLetters.replace(String.valueOf(userManager.getCurrentLetter()), "");
        }
        if(remainingTestLetters == ""){
            remainingTestLetters = null;
            userManager.saveTestResults(correctTestAnswers, wrongTestAnswers);
            ButtonManager buttonManager = LetterMenuPresenter.buttonManager;
            buttonManager.resetButtons(wrongTestAnswers);
            view.finishTest();
        }
        else{
            userManager.setCurrentLetter(remainingTestLetters.charAt(r.nextInt(remainingTestLetters.length())));
            soundManager.setCurrentLetter(userManager.getCurrentLetter());
            newRound();
        }
    }

    // c are chars already in use
    private char genUniqueChar(char ... c){
        char newChar = (char)(r.nextInt(26) + 'A');
        for(char i: c){
            if(i == Character.toUpperCase(newChar) || i == Character.toLowerCase(newChar)){
                return genUniqueChar(c);
            }
        }
        if(userManager.getCurrentCase() == Case.LOWERCASE){
            newChar = (char)(newChar+32);
        }
        else if(userManager.getCurrentCase() == Case.BOTH){
            int coinFlip = r.nextInt(2);
            newChar += 32*coinFlip;
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
        return userManager.getCurrentLetter();
    }

    public Case getCase(){
        return userManager.getCurrentCase();
    }

    public void playSound(){
        soundManager.playSound();
    }
}
