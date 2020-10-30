package com.example.soundcard;


public class StudentProfileContract {
    interface View{

    }

    interface Presenter{
        void loadProfile(String profile);
        String deleteStudent(String nameList);
        String editStudent(Student student);
    }
}
