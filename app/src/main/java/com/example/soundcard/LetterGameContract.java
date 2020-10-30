package com.example.soundcard;

public interface LetterGameContract {
    interface View{
        Boolean updateWinCounter(int wins);
        void openTestAlert();
        void openTeachingModel(char letter);
        void updateButtons(char nChar1, char nChar2, char nChar3);
        void finishTest();
    }

    interface Presenter{
        void loadBasicGame();
        void loadTest();
        boolean checkAnswer(char answer);
        char getCurrentLetter();
        Case getCase();
        void playSound();
    }
}
