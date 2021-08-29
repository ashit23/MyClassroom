package com.project.myclassroom1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class Submit extends AppCompatActivity {

    ImageView image1;
    EditText rollNo;
    StorageTask uploadTask;
    StorageReference storageReference;
    Uri imageUri;
    int REQUEST_CODE=1;
    Button submit;
    String myUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        storageReference = FirebaseStorage.getInstance().getReference("Submitted");

        rollNo=findViewById(R.id.rollno);


        image1=findViewById(R.id.test);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null){
                    ProgressDialog pd = new ProgressDialog(Submit.this);
                    pd.setMessage("Please Wait...");
                    pd.show();
                    StorageReference fileReference= storageReference.child(System.currentTimeMillis()
                            +""+getFileExtensions(imageUri));
                    uploadTask=fileReference.putFile(imageUri);
                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull @NotNull Task task) throws Exception {
                            if(!task.isComplete()){
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if(task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                assert downloadUri != null;
                                myUrl = downloadUri.toString();
                                String s = rollNo.getText().toString();
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Submitted").child(s);
                                String postDetails = reference1.push().getKey();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("image1", myUrl);
                                hashMap.put("roll", s);

                                assert postDetails != null;
                                reference1.setValue(hashMap);
                                pd.dismiss();
                                Toast.makeText(Submit.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(Submit.this,""+e,Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });

                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImagefrom();

            }
        });
    }

    private void openImagefrom() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
                image1.setImageURI(imageUri);
        }
    }


    private String getFileExtensions(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}