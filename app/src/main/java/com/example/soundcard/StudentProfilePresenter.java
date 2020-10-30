package com.example.soundcard;

public class StudentProfilePresenter {

    StudentProfileContract.View view;

    private Student studentProfile;

    public StudentProfilePresenter(StudentProfileContract.View view){
        this.view = view;
    }

    protected void loadProfile(String profile){
        if(profile != null){
            String[] info;
            String fName = "";
            String lName = "";
            String teacher = "";
            String age = "";
            String grade = "";
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
                    case "Age":
                        if(data.length>1)
                            age = data[1];
                        break;
                    case "Grade":
                        if(data.length>1)
                            grade = data[1];
                        break;
                }
            }
            studentProfile = new Student(fName, lName, teacher, grade, age);
        }
    }

    public String editStudent(Student student, String nameList){
        nameList = nameList.replaceFirst(studentProfile.getFirstName()+"\\+"+studentProfile.getLastName(), student.getFirstName()+"+"+student.getLastName());
        studentProfile = student;
        return nameList;
    }

    public Student getStudentProfile() {
        return studentProfile;
    }

    public String deleteStudent(String nameList){
        String newNames = nameList;
        String name = studentProfile.getFirstName()+"\\+"+studentProfile.getLastName();
        if(nameList.contains("|"+studentProfile.getFirstName()+"+"+studentProfile.getLastName())){
            newNames = nameList.replaceFirst("\\|"+name, "");
        }
        else{
            newNames = nameList.replaceFirst(name+"\\|", "");
        }
        return newNames;
    }
}
