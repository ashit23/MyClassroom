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
import com.project.myclassroom1.ui.home.ChooseClassAdapter;
import com.project.myclassroom1.ui.home.ChooseClassCalenderAdapter;
import com.project.myclassroom1.ui.home.ChooseClassTestAdapter;
import com.project.myclassroom1.ui.home.ClassAdapter;
import com.project.myclassroom1.ui.home.Classes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChooseScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    ChooseClassAdapter chooseClassAdapter;
    ChooseClassTestAdapter chooseClassTestAdapter;
    ChooseClassCalenderAdapter chooseClassCalenderAdapter;
    ArrayList<Classes> myClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_screen);

        String option = getIntent().getStringExtra("option");

        if(option.equals("Assignment")){

            recyclerView = findViewById(R.id.chooseRecycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            myClassList = new ArrayList<>();
            chooseClassAdapter = new ChooseClassAdapter(this, myClassList);
            recyclerView.setAdapter(chooseClassAdapter);
            myClass();
        }
        if(option.equals("Test")){
            recyclerView = findViewById(R.id.chooseRecycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            myClassList = new ArrayList<>();
            chooseClassTestAdapter = new ChooseClassTestAdapter(this, myClassList);
            recyclerView.setAdapter(chooseClassTestAdapter);
            myClassTest();
        }
        if(option.equals("Calender")){
            recyclerView = findViewById(R.id.chooseRecycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            myClassList = new ArrayList<>();
            chooseClassCalenderAdapter = new ChooseClassCalenderAdapter(this, myClassList);
            recyclerView.setAdapter(chooseClassCalenderAdapter);
            myClassCalender();
        }

    }
    private void myClassCalender() {
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
                    chooseClassCalenderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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
                    chooseClassAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void myClassTest() {
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
                    chooseClassTestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}