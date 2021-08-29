package com.project.myclassroom1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.myclassroom1.databinding.ActivityHomePageBinding;
import com.project.myclassroom1.ui.home.ClassAdapter;
import com.project.myclassroom1.ui.home.Classes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;
    private long backPressedTime;
    FloatingActionButton add,join;
    Dialog dailog;
    DatabaseReference databaseReference;
    HashMap<String, String> data;

    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    ArrayList<Classes> myClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHomePage.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       // NavigationUI.setupWithNavController(navigationView, navController);

        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,CreateClass.class));
            }
        });
        recyclerView = findViewById(R.id.classRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myClassList = new ArrayList<>();
        classAdapter = new ClassAdapter(this, myClassList);
        recyclerView.setAdapter(classAdapter);
        myClass();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.assignment) {
            Intent intent = new Intent(HomePage.this,ChooseScreen.class);
            intent.putExtra("option","Assignment");
            startActivity(intent);
        }
        if(id == R.id.test){
            Intent intent = new Intent(HomePage.this,ChooseScreen.class);
            intent.putExtra("option","Test");
            startActivity(intent);

        }
        if(id == R.id.calender){
            Intent intent = new Intent(HomePage.this,ChooseScreen.class);
            intent.putExtra("option","Calender");
            startActivity(intent);

        }
        if(id == R.id.submitted){
            Intent intent = new Intent(HomePage.this,Submitted.class);

            startActivity(intent);

        }
        if(id == R.id.result){
            Intent intent = new Intent(HomePage.this,ResultClass.class);

            startActivity(intent);

        }
        if (id == R.id.logout) {
            openDialog();
        }
        return false;
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
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


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(HomePage.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }


        backPressedTime = System.currentTimeMillis();

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