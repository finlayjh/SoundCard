package com.example.soundcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogConfirmAdvance extends AppCompatDialogFragment {

    private EditText editTextNewUser;
    private DialogConfirmAdvanceListener listener;
    private String userName;
    private TextView textViewConfirm;
    private int userLevel;

    public DialogConfirmAdvance(String userName){
        this.userName = userName;
        userLevel = 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_student_delete, null);

        textViewConfirm = view.findViewById(R.id.CONFIRM_TEXT);
        textViewConfirm.setText("Is " + userName + " already proficient with UPPERCASE letters?");

        final AlertDialog adialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.advanceUser(userLevel, userName);
                    }
                })
                .setPositiveButton("YES", null)
                .create();
        adialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(userLevel == 0){
                            userLevel++;
                            textViewConfirm.setText("Is " + userName + " ALSO already proficient with LOWERCASE letters?");
                        }
                        else if(userLevel == 1){
                            userLevel++;
                            listener.advanceUser(userLevel, userName);
                            adialog.dismiss();
                        }
                    }
                });
            }
        });
                /*.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(userLevel == 0){
                            userLevel++;
                            textViewConfirm.setText("Is " + userName + " ALSO already proficient with LOWERCASE letters?");
                        }
                        else if(userLevel == 1){
                            userLevel++;
                            listener.advanceUser(userLevel, userName);
                        }
                    }
                });*/
        return adialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogConfirmAdvanceListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DeleteUserDialogListener");
        }
    }

    public interface DialogConfirmAdvanceListener{
        void advanceUser(int level, String name);
    }
}