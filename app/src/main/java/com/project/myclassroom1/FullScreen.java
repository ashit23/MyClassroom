package com.project.myclassroom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class FullScreen extends AppCompatActivity {

    HashMap<String, String> data;
    EditText rollNo,marks;
    RelativeLayout layout;
    Button uploadMarks;
    ProgressDialog pd;
    ImageView imageView;
    DatabaseReference databaseReference;
    String intentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        imageView=findViewById(R.id.image);
        marks=findViewById(R.id.marksObtained);
        rollNo=findViewById(R.id.rollOfStudent);
        uploadMarks =findViewById(R.id.uploadMarks);
        layout=findViewById(R.id.marks);

        intentPass = getIntent().getStringExtra("uid");

        uploadMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(FullScreen.this);
                pd.setMessage("Please Wait..." );
                pd.show();

                String str_roll = rollNo.getText().toString();
                String str_marks = marks.getText().toString();

                if (TextUtils.isEmpty(str_roll)
                        || TextUtils.isEmpty(str_marks)) {
                    Toast.makeText(FullScreen.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }else {
                    uploadMarks(str_roll,str_marks);
                }
            }
        });
    }

    private void uploadMarks(String str_roll,String str_marks){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Marks").child(intentPass);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("roll", str_roll);
        hashMap.put("Marks", str_marks);
        databaseReference.setValue(hashMap);
        Toast.makeText(FullScreen.this, "Marks uploaded", Toast.LENGTH_SHORT).show();
        pd.dismiss();
        rollNo.setText("");
        marks.setText("");
    }

    @Override
    protected void onStart() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        data=new HashMap<>();
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data= (HashMap<String, String>) snapshot.getValue();
                if(snapshot.exists()){
                    if(data.get("userType").equals("Teacher")){
                        layout.setVisibility(View.VISIBLE);

                    }else{
                        layout.setVisibility(View.INVISIBLE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Submitted").child(intentPass).child("image1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                Picasso.get().load(link).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onStart();
    }

}