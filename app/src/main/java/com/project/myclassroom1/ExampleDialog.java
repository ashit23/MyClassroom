package com.project.myclassroom1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;

public class ExampleDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("LOG OUT!")
                .setMessage("Are you sure you want to log out of this account?")
                .setNegativeButton("no", (dialog, which) -> {
                    dialog.dismiss();
                    // startActivity(new Intent(getContext(),HomePage.class));
                })
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), MainActivity.class);

                    startActivity(intent);

                });
        return builder.create();
    }
}
