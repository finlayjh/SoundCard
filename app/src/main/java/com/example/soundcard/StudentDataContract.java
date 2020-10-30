package com.example.soundcard;

import java.util.ArrayList;

public interface StudentDataContract {
    interface View{

    }

    interface Presenter{
        ArrayList<String> getTestResultHistory();
        String getUserLvlText(int[] lvls);
        int getUserLevel(int[] lvls);
    }
}
