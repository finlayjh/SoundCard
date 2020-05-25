package com.example.soundcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.soundcard.R;
import com.example.soundcard.UserDeleteDialog;

public class SimplePopUpMessage extends AppCompatDialogFragment {

    private EditText editTextNewUser;
    private String popUpDialog;
    private TextView textViewConfirm;

    public SimplePopUpMessage(String dialog){
        popUpDialog = dialog;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_confirm_user_delete, null);

        textViewConfirm = view.findViewById(R.id.CONFIRM_TEXT);
        textViewConfirm.setText(popUpDialog);

        builder.setView(view)
                .setTitle(" ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        editTextNewUser = view.findViewById(R.id.edit_new_name);
        return builder.create();
    }
}
