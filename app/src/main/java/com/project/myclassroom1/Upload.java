package com.project.myclassroom1;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Upload extends AppCompatActivity {

    EditText link,teacher,subject;
    SimpleDateFormat simpleDateFormat;
    String Date;
    Button upload;
    DatabaseReference databaseReference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        link=findViewById(R.id.linkUpload);
        teacher=findViewById(R.id.teacher);
        upload=findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Upload.this);
                pd.setMessage("Please Wait..." );
                pd.show();

                String str_link = link.getText().toString();
                String str_teacher = teacher.getText().toString();

                if (TextUtils.isEmpty(str_link)
                        || TextUtils.isEmpty(str_teacher)) {
                    Toast.makeText(Upload.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }else {
                    upload(str_link,str_teacher);
                }
            }
        });
    }

    private void upload(String str_link,String str_teacher){
        String type = getIntent().getStringExtra("upload");
        Calendar calendar = Calendar.getInstance();

        simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date=simpleDateFormat.format(calendar.getTime());
        String class_name = getIntent().getStringExtra("className");
        databaseReference= FirebaseDatabase.getInstance().getReference("Upload").child(class_name).child(type).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Type", type);
        hashMap.put("Link", str_link);
        hashMap.put("TeacherName", class_name);
        hashMap.put("Date", Date);
        databaseReference.setValue(hashMap);
        Toast.makeText(Upload.this, "Uploaded", Toast.LENGTH_SHORT).show();
        pd.dismiss();
        startActivity(new Intent(Upload.this,ClassDetail.class));
    }
}