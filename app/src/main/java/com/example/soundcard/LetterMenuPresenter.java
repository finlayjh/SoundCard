package com.example.soundcard;

public class LetterMenuPresenter implements LetterMenuContract.Presenter {

    private LetterMenuContract.View view;
    static UserManager userManager;
    static SoundManager soundManager;
    static ButtonManager buttonManager;

    private int consecutiveWinsCounter;
    private char selectedLetter;

    public LetterMenuPresenter(LetterMenuContract.View view, SoundManager soundManager, ButtonManager buttonManager){
        this.view = view;
        userManager = LevelSelectPresenter.userManager;
        this.soundManager = soundManager;
        this.buttonManager = buttonManager;
        buttonManager.swapToCase(userManager.getCurrentCase(), userManager.getUserLetters());

        consecutiveWinsCounter = 0;
    }

    public void onBasicGameResult(){
        buttonManager.loadUserButtons(Character.toString(selectedLetter));
        userManager.addUserLetter(selectedLetter);
        consecutiveWinsCounter++;
        selectedLetter = '\0';
        if(consecutiveWinsCounter == 2){
            consecutiveWinsCounter =0;
            view.loadTest();
        }
    }

    public void setSelectedLetter(char c){
        selectedLetter = c;
        userManager.setCurrentLetter(c);
        soundManager.setCurrentLetter(c);
    }

    public void onDestroy(){
        soundManager.onDestroy();
    }

    public Case getCase(){return userManager.getCurrentCase();}
}
