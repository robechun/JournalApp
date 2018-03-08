package com.example.robertchung.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mRegisterButton;
    private TextView mSignInTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mEmailEditText = (EditText) findViewById(R.id.emailRegisterEditTextView);
        mPasswordEditText = (EditText) findViewById(R.id.passwordRegisterEditTextView);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordRegisterEditTextView);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mSignInTextView = (TextView) findViewById(R.id.back_to_sign_in);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        // If you say you have an account, then go back to login page
        mSignInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmailEditText.getText().toString();
                String pass = mPasswordEditText.getText().toString();
                String confPass = mConfirmPasswordEditText.getText().toString();

                // Check if fields are good
                if (validEmail(email) && validPass(pass, confPass)){
                    // Fields worked, go register!
                    createAccountEmail(email, pass);
                } else {
                    Toast.makeText(RegisterActivity.this, "Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    // Very simple check to see that it is a valid email
    // TODO another version: validate through email confirmation
    private boolean validEmail(String email) {
        if (email.contains("@")) {

            return true;
        }

        return false;
    }

    // Simple checking to see password matches
    // TODO: maybe have set stuff to make sure password is stronk enough
    private boolean validPass(String pass, String confPass) {
        if (pass.equals(confPass)){
            return true;
        }

        return false;
    }


    // createAccountEmail creates an account with the given email
    //  and password and logs in the user.
    private void createAccountEmail(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, move to home screen
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    // updateUI first populates the database with the new user and then goes to the main activity.
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Success, add entry to Firebase database and go to main Activity

            // Make new user account
            String uid = user.getUid();
            String em = mEmailEditText.getText().toString();
            Account newAcc = new Account(em, false);

            Calendar calendar = Calendar.getInstance();
            String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()).replaceAll("/","");


            // Send to database
            mDatabase.child("Users").child(uid).setValue(newAcc);
            mDatabase.child("Users").child(uid).child("Entries").child(currentDate).setValue(new Journal());

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        } else {
            Log.w(TAG, "not going to mainActivity");
        }
    }

}


// TODO: catch if user is already registered (?)
// TODO: toasts
// TODO: technically if successful should not let user touch stuff
