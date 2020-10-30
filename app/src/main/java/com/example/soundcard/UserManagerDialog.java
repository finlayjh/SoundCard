package com.example.soundcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UserManagerDialog extends AppCompatDialogFragment {

    private UserManagerDialog.UserManagerDialogListener listener;
    private Button btnUppercaseClear;
    private Button btnUppercaseAdvance;
    private Button btnLowercaseClear;
    private Button btnLowercaseAdvance;
    private Button btnSoundsClear;
    private int[] userLetterCount;
    private int[] userChanges;

    public UserManagerDialog(int[] userLetterCount) {
        this.userLetterCount = userLetterCount;
        userChanges = new int[]{-1,-1,-1};
        for(int i = 0; i<3; i++){
            if(userLetterCount[i] == 0){
                userChanges[i] = 0;
            }
            else if(userLetterCount[i] == 26){
                userChanges[i] = 1;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_progress_manager, null);

        loadButtons(view);

        updateButtons();

        builder.setView(view)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.updateUser(userChanges);
                    }
                });
        return builder.create();
    }

    public void updateButtons(){
        btnUppercaseClear.setEnabled(userChanges[0] != 0);
        btnLowercaseClear.setEnabled(userChanges[1] != 0);
        btnSoundsClear.setEnabled(userChanges[2] != 0);
        btnUppercaseAdvance.setEnabled(userChanges[0] != 1);
        btnLowercaseAdvance.setEnabled(userChanges[1] != 1);
        if(!btnUppercaseAdvance.isEnabled() && btnUppercaseAdvance.getText() != "Completed"){
            btnUppercaseAdvance.setText("Completed");
        }
        else if(btnUppercaseAdvance.isEnabled() && btnUppercaseAdvance.getText() != "Advance"){
            btnUppercaseAdvance.setText("Advance");
        }
        if(!btnLowercaseAdvance.isEnabled() && btnLowercaseAdvance.getText() != "Completed"){
            btnLowercaseAdvance.setText("Completed");
        }
        else if(btnLowercaseAdvance.isEnabled() && btnLowercaseAdvance.getText() != "Advance"){
            btnLowercaseAdvance.setText("Advance");
        }
    }

    public void clearOnClick(Case mCase){
        Log.d("WOW", "AMAZIN clear");
        switch (mCase){
            case UPPERCASE:
                userChanges[0] = 0;
                break;
            case LOWERCASE:
                userChanges[1] = 0;
                break;
            case BOTH:
                userChanges[2] = 0;
                break;
        }
        updateButtons();
    }

    public void advanceOnClick(Case mCase){
        Log.d("WOW", "AMAZIN advance");
        switch (mCase) {
            case UPPERCASE:
                userChanges[0] = 1;
                break;
            case LOWERCASE:
                userChanges[1] = 1;
                break;
        }
        updateButtons();
    }

    private void loadButtons(View view){
        btnUppercaseClear = view.findViewById(R.id.btn_uppercase_clear);
        btnUppercaseAdvance = view.findViewById(R.id.btn_uppercase_advance);
        btnLowercaseClear = view.findViewById(R.id.btn_lowercase_clear);
        btnLowercaseAdvance = view.findViewById(R.id.btn_lowercase_advance);
        btnSoundsClear = view.findViewById(R.id.btn_sounds_clear);

        btnUppercaseClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearOnClick(Case.UPPERCASE);
            }
        });
        btnLowercaseClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearOnClick(Case.LOWERCASE);
            }
        });
        btnSoundsClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearOnClick(Case.BOTH);
            }
        });
        btnUppercaseAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceOnClick(Case.UPPERCASE);
            }
        });
        btnLowercaseAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceOnClick(Case.LOWERCASE);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (UserManagerDialog.UserManagerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement UserManagerDialogListener");
        }
    }

    public interface UserManagerDialogListener {
        void updateUser(int[] newInfo);
    }
}
