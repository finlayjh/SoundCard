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

public class UserDeleteDialog extends AppCompatDialogFragment {

    private EditText editTextNewUser;
    private DeleteUserDialogListener listener;
    private String deleteUserName;
    private TextView textViewConfirm;

    public UserDeleteDialog(String deleteName){
        deleteUserName = deleteName;

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
        textViewConfirm.setText("Delete " + deleteUserName + "?");

        builder.setView(view)
                .setTitle(" ")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("CONFIRM?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean confirm = true;
                        listener.deleteUser(confirm);
                    }
                });
        editTextNewUser = view.findViewById(R.id.edit_new_name);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteUserDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DeleteUserDialogListener");
        }
    }

    public interface DeleteUserDialogListener{
        void deleteUser(Boolean confirm);
    }
}
