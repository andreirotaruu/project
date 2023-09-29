package com.example.mobileapps2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewFirstName, textViewLastName, textViewRegisterDate, textViewEmail;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    private DatabaseReference database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Home");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        findViews();
        signOut();
        Update();

        //show profile details if the user is not null

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null){
            //progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }else{
            Toast.makeText(UserProfileActivity.this, "User not found", Toast.LENGTH_SHORT).show();
        }

    }

    private void Update() {
        Button update = findViewById(R.id.button_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePage = new Intent(UserProfileActivity.this, UpdatePage.class);
                startActivity(updatePage);
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        //to obtain metadata about the user
        FirebaseUserMetadata metadata = firebaseUser.getMetadata();
        //grab the register data of the user
        long registerTimeStamp = metadata.getCreationTimestamp();
        //define a pattern for our data
        String datePattern = "E,dd MMM yyy:mm a z"; // day,dd MMM hh: mm AM/PM timezone
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        sdf.setTimeZone(TimeZone.getDefault());
        String register = sdf.format(new Date(registerTimeStamp));
        String registerDate = getResources().getString(R.string.user_since, register);
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        String welcome = getResources().getString(R.string.welcome_user);
        textViewEmail.setText(email);
        textViewFirstName.setText(name);
        textViewWelcome.setText(welcome);
        progressBar.setVisibility(View.GONE);

    }

        private void signOut() {
        Button buttonSignOut = findViewById(R.id.button_sign_out);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfileActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                //clear the stack so the user cannot use the back arrow to get back in
                Intent mainActivity = new Intent(UserProfileActivity.this, MainActivity2.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                finish();
            }
        });

    }

    private void findViews() {
        textViewWelcome = findViewById(R.id.textView_welcone);
        textViewFirstName = findViewById(R.id.textView_show_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewRegisterDate = findViewById(R.id.textView_show_register_date);
        progressBar = findViewById(R.id.progressbar);



    }
}