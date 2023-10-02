package com.example.mobileapps2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class UpdatePage extends AppCompatActivity {
    private TextView editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page);
        getSupportActionBar().setTitle("Update");
        auth = FirebaseAuth.getInstance();
        findViews();
        updateInfo();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        showUserProfile(firebaseUser);
        if(firebaseUser != null){
            //progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }else{
            Toast.makeText(UpdatePage.this, "User not found", Toast.LENGTH_SHORT).show();
        }



    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        editTextEmail.setText(email);
        editTextFirstName.setText(name);
        //progressBar.setVisibility(View.GONE);

    }
    private void findViews() {
        editTextLastName = findViewById(R.id.textView_show_lname);
        editTextFirstName = findViewById(R.id.textView_show_fname);
        editTextEmail = findViewById(R.id.textView_show_email);
        editTextPassword = findViewById(R.id.textView_show_pwd);
    }

    private void updateInfo(){
        Button update = findViewById(R.id.button_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextEmail .getText().toString();
                String textFirstName = editTextFirstName.getText().toString();
                String textLastName = editTextLastName .getText().toString();
                String textPwd = editTextPassword .getText().toString();
                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(UpdatePage.this, "Change Your Email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("New Email is Required");
                }else if (TextUtils.isEmpty(textFirstName)) {
                    Toast.makeText(UpdatePage.this, "Change Your First Name", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("New First Name is Required");
                }else if(TextUtils.isEmpty(textLastName)) {
                    Toast.makeText(UpdatePage.this, "Change Your Last Name", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("New Last Name is Required");
                }else if(TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(UpdatePage.this, "Change Your Password", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("New Password is Required");
                }else  if((textPwd.length() < 6)) {
                    Toast.makeText(UpdatePage.this, "Please enter your password longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password is required to be more than 6 characters");
                    editTextPassword.requestFocus();
                }else{
                    UpdateUser(textEmail,textPwd,textFirstName,textLastName);
                }
            }
        });
    }



    private void UpdateUser(String textEmail, String textPassword, String textFirstName, String textLastName) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textFirstName + " " + textLastName).build();
        firebaseUser.updateProfile(profileUpdates);
        firebaseUser.updateEmail(textEmail);
        database.child("users").child(firebaseUser.getUid()).child("Email").setValue(textEmail);


        auth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(UpdatePage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UpdatePage.this, "Information Updated", Toast.LENGTH_SHORT).show();
                    Intent userProfileActivity = new Intent(UpdatePage.this, UserProfileActivity.class);
                    startActivity(userProfileActivity);
                }else{
                    try {
                        throw task.getException();
                    }catch(Exception e){
                        Toast.makeText(UpdatePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }
}