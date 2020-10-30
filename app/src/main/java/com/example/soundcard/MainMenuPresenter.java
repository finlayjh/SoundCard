package com.example.soundcard;

import android.util.Log;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class MainMenuPresenter implements MainMenuContract.Presenter {

    MainMenuContract.View view;

    private ArrayList<Student> profileList = new ArrayList<>();
    private ArrayList<String> filterList = new ArrayList<>();
    private ArrayList<String> studentList = new ArrayList<>();

    public MainMenuPresenter(MainMenuContract.View view){
        this.view = view;
        filterList.add("Alphabetical");
        filterList.add("Teacher");
        filterList.add("Grade");
        filterList.add("Age");
    }

    public String[] loadNameList(String longNameList){
        String[] names;
        if(longNameList != null) {
            names = longNameList.split("\\|");
            for(String name: names){
                studentList.add(name);
            }
            return names;
        }
        return null;
    }

    public void loadStudent(String profile){
        if(profile != null){
            String[] info;
            String fName = "";
            String lName = "";
            String teacher = "";
            String grade = "";
            String age = "";
            info = profile.split("\\|");
            for(String i : info){
                String[] data = i.split(":");
                switch (data[0]){
                    case "FName":
                        if(data.length>1)
                            fName = data[1];
                        break;
                    case "LName":
                        if(data.length>1)
                            lName = data[1];
                        break;
                    case "Teacher":
                        if(data.length>1)
                            teacher = data[1];
                        break;
                    case "Grade":
                        if(data.length>1)
                            grade = data[1];
                        break;
                    case "Age":
                        if(data.length>1)
                            age = data[1];
                        break;
                }
            }
            profileList.add(new Student(fName, lName, teacher, grade, age));
        }
    }

    public String packageStudentList(){
        String names = "";
        for (int i = 0; i < studentList.size(); i++) {
            names += studentList.get(i);
            Log.d("fuck me", "names2: " + studentList.get(i));
            if (i != studentList.size() - 1) {
                names += "|";
            }
        }
        return names;
    }

    public boolean saveNewStudent(Student student){
        Log.d("fuck me", "tesing: " + student.getFirstName() + " and " + student.getLastName());
        for(Student s: profileList){
            if(s.getFirstName().equals(student.getFirstName()) && s.getLastName().equals(student.getLastName())){
                return false;
            }
        }
        profileList.add(student);
        studentList.add(student.getFirstName() + "+" + student.getLastName());
        view.updateStudentList();
        view.addStudentProfile(student);
        return true;
    }

    public ArrayList<String> getFilterList(){
        return filterList;
    }

    public ArrayList<String> getFilteredStudentList(String filter){
        ArrayList<String> filteredList =  new ArrayList<>();
        Log.d("Filter", "filter: "+filter);
        switch (filter){
            case "Alphabetical":
                Collections.sort(profileList, new Comparator<Student>() {
                    public int compare(Student s1, Student s2) {
                        return s1.getFormatedName().compareTo(s2.getFormatedName());
                    }
                });
                for(Student student: profileList){
                    if(!filteredList.contains("-" + student.getFormatedName().substring(0,1).toUpperCase() + "-")){
                        filteredList.add("-" + student.getFormatedName().substring(0,1).toUpperCase() + "-");
                    }
                    filteredList.add(student.getFormatedName());
                }
                break;
            case "Teacher":
                Collections.sort(profileList, new Comparator<Student>() {
                    public int compare(Student s1, Student s2) {
                        return s1.getTeacher().compareTo(s2.getTeacher());
                    }
                });
                for(Student student: profileList){
                    if(!filteredList.contains("-" + student.getTeacher() + "-")){
                        filteredList.add("-" + student.getTeacher() + "-");
                    }
                    filteredList.add(student.getFormatedName());
                }
                break;
            case "Grade":
                Collections.sort(profileList, new Comparator<Student>() {
                    public int compare(Student s1, Student s2) {
                        return s1.getGrade().compareTo(s2.getGrade());
                    }
                });
                for(Student student: profileList){
                    if(!filteredList.contains("-" + student.getGrade() + "-")){
                        filteredList.add("-" + student.getGrade() + "-");
                    }
                    filteredList.add(student.getFormatedName());
                }
                break;
            case "Age":
                Collections.sort(profileList, new Comparator<Student>() {
                    public int compare(Student s1, Student s2) {
                        return s1.getAge().compareTo(s2.getAge());
                    }
                });
                for(Student student: profileList){
                    if(!filteredList.contains("-" + student.getAge() + "-")){
                        filteredList.add("-" + student.getAge() + "-");
                    }
                    filteredList.add(student.getFormatedName());
                }
                break;
        }
        return filteredList;
    }

    public String getCurrentStudentName(String formName){
        for(Student student: profileList){
            if(student.getFormatedName() == formName){
                return student.getFirstName()+"+"+student.getLastName();
            }
        }
        return null;
    }
}
