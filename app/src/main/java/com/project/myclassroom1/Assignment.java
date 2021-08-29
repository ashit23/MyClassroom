package com.project.myclassroom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.myclassroom1.ui.home.Details;
import com.project.myclassroom1.ui.home.DetailsAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class Assignment extends AppCompatActivity {

    RecyclerView recyclerView;
    DetailsAdapter detailsAdapter;
    ArrayList<Details> myDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        recyclerView = findViewById(R.id.assignmentRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDetailList = new ArrayList<>();
        detailsAdapter = new DetailsAdapter(this, myDetailList);
        recyclerView.setAdapter(detailsAdapter);
        myDetails();
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
    }

}