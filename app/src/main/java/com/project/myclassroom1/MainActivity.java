package com.project.myclassroom1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button login;
    TextView registerup,googlesignin,forgotPass;
    EditText email;
    FirebaseUser firebaseUser;
    TextInputEditText passwordI;
    Dialog dailog;
    HashMap<String, String> data;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private static int RC_SIGN_IN=100;
    private static int RCC_SIGN_IN=101;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        googlesignin=findViewById(R.id.gsignin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        forgotPass=findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your email to receive reset link");
                passwordResetDialog.setView(resetmail);

                passwordResetDialog.setPositiveButton("yes", (dialog, which) -> {
                    String mail = resetmail.getText().toString();
                    auth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> Toast.makeText(MainActivity.this, "Reset link set to your email",
                            Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error! please try again" + e.getMessage(),
                            Toast.LENGTH_SHORT).show());
                });
                passwordResetDialog.setNegativeButton("No", (dialog, which) -> {

                });

                passwordResetDialog.create().show();
            }
        });
        email=findViewById(R.id.descIndividual);
        passwordI=findViewById(R.id.password);
        login=findViewById(R.id.login);
        data=new HashMap<>();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        login.setOnClickListener(v -> {
            ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.show();

            String str_mobile = email.getText().toString();
            String str_password = passwordI.getText().toString();
            if (TextUtils.isEmpty(str_mobile) || TextUtils.isEmpty(str_password)) {
                Toast.makeText(MainActivity.this, "All fields are required",
                        Toast.LENGTH_SHORT).show();
                pd.dismiss();
            } else {
                auth.signInWithEmailAndPassword(str_mobile, str_password)
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(auth.getCurrentUser().getUid());

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        pd.dismiss();
                                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        pd.dismiss();
                                    }
                                });
                            } else {
                                pd.dismiss();
                                Toast.makeText(MainActivity.this, "Please enter correct credentials",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        registerup=findViewById(R.id.registerup);
        registerup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });


    }

    private void dialog() {
        dailog = new Dialog(this);
        dailog.setContentView(R.layout.popup_layout);
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
        TextView teacher = dailog.findViewById(R.id.teacher);
        TextView student = dailog.findViewById(R.id.student);
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // languageManager.updateResource("hi");
                // recreate();
                signIn();
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // languageManager.updateResource("en");
                // recreate();
                signIn1();
            }
        });

        dailog.show();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signIn1() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RCC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if (requestCode == RCC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult1(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FirebaseUser firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String name = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Users").child(name);
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("userType", "Teacher");
            hashMap.put("joinedClasses","");
            databaseReference.setValue(hashMap);

            startActivity(new Intent(MainActivity.this,HomePage.class));
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void handleSignInResult1(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FirebaseUser firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String name = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Users").child(name);
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("userType", "Student");
            hashMap.put("joinedClasses","");
            databaseReference.setValue(hashMap);

            startActivity(new Intent(MainActivity.this,HomePage.class));
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Checking your Details...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        startActivity(new Intent(MainActivity.this, HomePage.class));
                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }
}