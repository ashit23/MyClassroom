package com.project.myclassroom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.myclassroom1.ui.home.ClassAdapter;
import com.project.myclassroom1.ui.home.Classes;
import com.project.myclassroom1.ui.home.Details;
import com.project.myclassroom1.ui.home.DetailsAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ClassDetail extends AppCompatActivity {

    HashMap<String, String> data;
    Dialog dailog;
    FloatingActionButton add;


    RecyclerView recyclerView;
    DetailsAdapter detailsAdapter;
    ArrayList<Details> myDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        recyclerView = findViewById(R.id.itemRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDetailList = new ArrayList<>();
        detailsAdapter = new DetailsAdapter(this, myDetailList);
        recyclerView.setAdapter(detailsAdapter);
        myDetails();
    }

    private void dialog() {
        dailog = new Dialog(this);
        dailog.setContentView(R.layout.create_popup);
        dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailog.setCancelable(false);
        dailog.setTitle("Choose Your language");
        ImageView closeBtn = dailog.findViewById(R.id.closeDailogeLanguage);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog.dismiss();
            }
        });
        TextView assignment = dailog.findViewById(R.id.assignment);
        TextView test = dailog.findViewById(R.id.test);
        TextView classLink = dailog.findViewById(R.id.classLink);
        String calss_name = getIntent().getStringExtra("className");
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassDetail.this,Upload.class);
                intent.putExtra("upload","Assignment Link");
                intent.putExtra("className", calss_name);
                startActivity(intent);
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassDetail.this,Upload.class);
                intent.putExtra("upload","Test Link");
                intent.putExtra("className", calss_name);
                startActivity(intent);
            }
        });
        classLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassDetail.this,Upload.class);
                intent.putExtra("upload","Class Link");
                intent.putExtra("className", calss_name);
                startActivity(intent);
            }
        });

        dailog.show();
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
                        add.setVisibility(View.VISIBLE);

                    }else{
                        add.setVisibility(View.INVISIBLE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onStart();
    }


    private void myDetails() {
        String class_name = getIntent().getStringExtra("className");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Upload").child(class_name).child("Assignment Link");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //myDetailList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Details details = snapshot1.getValue(Details.class);
                    myDetailList.add(details);

                    Collections.reverse(myDetailList);
                    detailsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Upload").child(class_name).child("Test Link");
        reference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Details details = snapshot1.getValue(Details.class);
                    myDetailList.add(details);

                    Collections.reverse(myDetailList);
                    detailsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Upload").child(class_name).child("Class Link");
        reference2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Details details = snapshot1.getValue(Details.class);
                    myDetailList.add(details);

                    Collections.reverse(myDetailList);
                    detailsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}