package com.example.soundcard;

import android.content.Intent;

public interface LetterMenuContract {
    interface View{
        void loadTest();
    }
    interface Presenter{
        void onBasicGameResult();
        void setSelectedLetter(char c);
        void onDestroy();
    }
}
