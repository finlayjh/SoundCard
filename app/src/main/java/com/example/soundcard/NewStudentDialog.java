package com.example.soundcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.regex.Pattern;

public class NewStudentDialog extends AppCompatDialogFragment {

    private String letters = "[a-zA-Z]*";
    private String numbers = "[0-9]*";

    private EditText etFirstName, etLastName, etTeacher, etGrade, etAge;
    String fName, lName, teacher, grade, age, title;
    private NewUserDialogListener listener;
    private TextView tvTitle;
    private Context mContext;

    public NewStudentDialog(Student student){
        if(student != null){
            fName = student.getFirstName();
            lName = student.getLastName();
            teacher = student.getTeacher();
            grade = student.getGrade();
            age = student.getAge();
            title = "Edit Student";
        }
        else{
            title = "New Student";
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_student, null);
        tvTitle = new TextView(view.getContext());
        tvTitle.setText(title);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextSize(20f);

        builder.setView(view)
                .setCustomTitle(tvTitle)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fName = etFirstName.getText().toString();
                        lName = etLastName.getText().toString();
                        teacher = etTeacher.getText().toString();
                        grade = etGrade.getText().toString();
                        age = etAge.getText().toString();
                        listener.saveNewStudent(fName, lName, teacher, grade, age);
                    }
                });
        etFirstName = view.findViewById(R.id.edit_first_name);
        if(fName != null && !fName.equals("")){
            etFirstName.setText(fName);
        }
        etLastName = view.findViewById(R.id.edit_last_name);
        if(lName != null && !lName.equals("")){
            etLastName.setText(lName);
        }
        etTeacher = view.findViewById(R.id.edit_teacher);
        if(teacher != null && !teacher.equals("")){
            etTeacher.setText(teacher);
        }
        etGrade = view.findViewById(R.id.edit_grade);
        if(grade != null && !grade.equals("")){
            etGrade.setText(grade);
        }
        etAge = view.findViewById(R.id.edit_age);
        if(age != null && age.equals("")){
            etAge.setText(age);
        }
        final AlertDialog dialog = builder.create();
        dialog.show();

        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(isValidAnswers(dialog));

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidAnswers(dialog)){
                    etFirstName.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    etFirstName.getBackground().clearColorFilter();
                }
            }
        });
        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidAnswers(dialog)){
                    etLastName.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    etLastName.getBackground().clearColorFilter();
                }
            }
        });
        etTeacher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidAnswers(dialog)){
                    etTeacher.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    etTeacher.getBackground().clearColorFilter();
                }
            }
        });
        etGrade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("OI TESTIng69", "matches?: " + etGrade.getText().toString().matches("[a-zA-Z_0-9]*"));
                if(!isValidAnswers(dialog)){
                    Log.d("OI TESTIng699999", "fuck me more???? " + etGrade.getBackground());
                    etGrade.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    Log.d("OI TESTIng699999", "fuck me " + etGrade.getBackground());
                    etGrade.getBackground().clearColorFilter();
                }
            }
        });
        etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidAnswers(dialog)){
                    etAge.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    etAge.getBackground().clearColorFilter();
                }
            }
        });

        return dialog;
    }

    private boolean isValidAnswers(AlertDialog dialog){

        //valid first name
        if(!etFirstName.getText().toString().matches(letters)){
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            return false;
        }
        //valid last name
        if(!etLastName.getText().toString().matches(letters)){
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            return false;
        }
        //valid teacher name
        if(!etTeacher.getText().toString().matches(letters)){
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            return false;
        }
        //valid grade
        Log.d("OI TESTIng", "matches?: " + etGrade.getText().toString().matches("[a-zA-Z_0-9]*"));
        if(!etGrade.getText().toString().matches("[a-zA-Z_0-9]*")){
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            return false;
        }
        //valid age
        if(!etAge.getText().toString().matches(numbers)){
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            return false;
        }
        //last name not empty
        if(etLastName.getText().toString().equals("") || etLastName.equals(null)){
            Toast.makeText(mContext, R.string.last_name_required, Toast.LENGTH_SHORT).show();
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            return true;
        }
        else{
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            return true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            listener = (NewUserDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement NewClassDialogListener");
        }
    }

    public interface NewUserDialogListener{
        void saveNewStudent(String fName, String lName, String teacher, String grade, String age);
    }
}
