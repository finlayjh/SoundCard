package com.example.soundcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class NewUserDialog extends AppCompatDialogFragment {

    private EditText editTextNewUser;
    private NewUserDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_user_dialog, null);

        builder.setView(view)
                .setTitle("Enter new name")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editTextNewUser.getText().toString();
                        listener.saveNewUser(name);
                    }
                });
        editTextNewUser = view.findViewById(R.id.edit_new_name);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NewUserDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement NewClassDialogListener");
        }
    }

    public interface NewUserDialogListener{
        void saveNewUser(String name);
    }
}
