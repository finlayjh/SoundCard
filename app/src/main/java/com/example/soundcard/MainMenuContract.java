package com.example.soundcard;

import java.util.ArrayList;

public interface MainMenuContract {
    interface View{
        void updateStudentList();
        void addStudentProfile(Student student);
    }

    interface Presenter{
        void loadStudent(String profile);
        String[] loadNameList(String nameList);
        String packageStudentList();
        ArrayList<String> getFilteredStudentList(String filter);
        ArrayList<String> getFilterList();
        boolean saveNewStudent(Student student);
    }
}
