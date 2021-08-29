package com.project.myclassroom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextView login;
    ProgressDialog pd;
    EditText name;
    FirebaseAuth auth;
    TextInputEditText password;
    CheckBox teacher,student;
    DatabaseReference databaseReference;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login=findViewById(R.id.loginup);
        name=findViewById(R.id.name);
        register=findViewById(R.id.register);
        password=findViewById(R.id.editTextTextPersonName5);
        student=findViewById(R.id.student);
        teacher=findViewById(R.id.teacher);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,MainActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Register.this);
                pd.setMessage("Please Wait..." );
                pd.show();

                String str_email = name.getText().toString();
                String str_password = password.getText().toString();

                if (TextUtils.isEmpty(str_email)
                        || TextUtils.isEmpty(str_password)) {
                    Toast.makeText(Register.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (str_password.length() < 6) {
                    Toast.makeText(Register.this, "password must be" +
                                    " of minimum six characters",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (!teacher.isChecked() && !student.isChecked()) {
                    Toast.makeText(Register.this, "please tell us if you are a teacher or a student",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (teacher.isChecked() && student.isChecked()) {
                    Toast.makeText(Register.this, "please select only one option that if you're teacher or a student",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    register(str_email, str_password);
                }
            }
        });

    }

    private void register(  String str_email,
                            String str_password) {
        auth.createUserWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String name = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference("Users").child(name);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            if (teacher.isChecked()) {
                                hashMap.put("name", name);
                                hashMap.put("userType", "Teacher");
                                hashMap.put("userEmail", str_email);
                                hashMap.put("joinedClasses","");
                            } else if (student.isChecked()) {
                                hashMap.put("name", name);
                                hashMap.put("userType", "Student");
                                hashMap.put("userEmail", str_email);
                                hashMap.put("joinedClasses","");
                            }

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                            Intent intent = new Intent(Register.this, HomePage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(Register.this, "Please register with a different Mail Id",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}