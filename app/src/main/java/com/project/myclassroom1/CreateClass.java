package com.project.myclassroom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

public class CreateClass extends AppCompatActivity {

    EditText name,subject;
    Button create;
    ProgressDialog pd;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        name= findViewById(R.id.nameClass);
        subject=findViewById(R.id.Subject);
        create=findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(CreateClass.this);
                pd.setMessage("Please Wait..." );
                pd.show();

                String str_name = name.getText().toString();
                String str_teacher = subject.getText().toString();

                if (TextUtils.isEmpty(str_name)
                        || TextUtils.isEmpty(str_teacher)) {
                    Toast.makeText(CreateClass.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }else {
                    create(str_name,str_teacher);
                }
            }
        });
    }

    private void create(String str_name,String str_teacher){
        databaseReference= FirebaseDatabase.getInstance().getReference("Classes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+str_name);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ClasName", str_name);
        hashMap.put("TeacherName", str_teacher);
        databaseReference.setValue(hashMap);
        Toast.makeText(CreateClass.this, "Class created", Toast.LENGTH_SHORT).show();
        pd.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            ProgressDialog progressDialog = new ProgressDialog(CreateClass.this);
            progressDialog.setMessage("Checking your Details...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(userReference.child("userType").toString().equals("Student")){
                            Toast.makeText(CreateClass.this, "This function is only enabled for teachers", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateClass.this, HomePage.class));
                        }

                        progressDialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }
}