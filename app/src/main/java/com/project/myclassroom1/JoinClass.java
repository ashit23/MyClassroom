package com.project.myclassroom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.myclassroom1.ui.home.ClassAdapter;
import com.project.myclassroom1.ui.home.Classes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinClass extends AppCompatActivity {

    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    ArrayList<Classes> myClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);

        recyclerView = findViewById(R.id.classRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myClassList = new ArrayList<>();
        classAdapter = new ClassAdapter(this, myClassList);
        recyclerView.setAdapter(classAdapter);
        myClass();

    }

    private void myClass() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                myClassList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Classes classes = snapshot1.getValue(Classes.class);
                        myClassList.add(classes);

                    //Collections.reverse(myClassList);
                    classAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}