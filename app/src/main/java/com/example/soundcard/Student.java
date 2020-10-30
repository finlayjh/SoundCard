package com.example.soundcard;

public class Student {
    private String firstName;
    private String lastName;
    private String formatedName;
    private String teacher;
    private String age;
    private String grade;
    protected Student(String fName, String lName, String teacher, String grade, String age){
        firstName = fName;
        lastName = lName;
        this.teacher = teacher;
        this.grade = grade;
        this.age = age;
        if(lastName != null){
            String preformatedName = lastName;
            if(!firstName.equals("")){
                preformatedName += ", " + firstName;
            }
            formatedName = preformatedName;
        }
        else{
            formatedName = firstName;
        }
    }
    protected String getFirstName(){return firstName;}
    protected String getLastName(){return lastName;}
    protected String getTeacher(){return teacher;}
    protected String getGrade(){return grade;}
    protected String getAge(){return age;}
    protected String getFormatedName(){return formatedName;}
    protected String getPackagedStudent(){
        String profile = "";
        profile += "FName:" + firstName;
        profile += "|LName:" + lastName;
        profile += "|Teacher:" + teacher;
        profile += "|Grade:" + grade;
        profile += "|Age:" + age;
        return profile;
    }
}
