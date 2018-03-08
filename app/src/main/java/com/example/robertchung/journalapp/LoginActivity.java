package com.example.robertchung.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private CallbackManager mCallbackManager;
    private Button mSignInButton;
    private EditText mEmailEditTextView;
    private EditText mPasswordEditTextView;
    private TextView mSignUpTextView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;










    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Auth for Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailEditTextView = (EditText) findViewById(R.id.emailLoginEditTextView);
        mPasswordEditTextView = (EditText) findViewById(R.id.passwordLoginEditTextView);
        mSignUpTextView = (TextView) findViewById(R.id.sign_up_text);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        // Try signing in with given email/password
        mSignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = mEmailEditTextView.getText().toString();
                String pass = mPasswordEditTextView.getText().toString();
                signInEmail(email, pass);
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }







    // Handles facebook login.
    // If successful, checks if facebook user is already in our system and updates database if not.
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final ConstraintLayout layout = this.findViewById(R.id.login);
        enableViews(layout, false);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, move to home screen
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            checkUserExists(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            enableViews(layout, true);
                        }

                    }
                });
    }





    // signInEmail attempts to sign into our application with the given email/password
    // if successful, move onto the next screen.
    private void signInEmail(String email, String password) {
        final ConstraintLayout layout = this.findViewById(R.id.login);
        enableViews(layout, false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            enableViews(layout, true);
                        }

                    }
                });
    }




    // TODO: called when success/fail happens
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Success, go to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            final ConstraintLayout layout = this.findViewById(R.id.login);
            enableViews(layout, true);
            clearViews();
        } else {
            Log.w(TAG, "not going to mainActivity");
        }



//        hideProgressDialog();
//        if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
//        }
    }

    // enableViews either enables or disables all the views within a view
    //  to ensure no fishy stuff from the user
    private void enableViews(ViewGroup v, boolean enabled) {
        int childCount = v.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (v.getChildAt(i) instanceof ViewGroup) {
                enableViews((ViewGroup) v.getChildAt(i), enabled);
            } else {
                v.getChildAt(i).setEnabled(enabled);
            }
        }
    }

    // checkUserExists checks whether or not a user exists
    //  This is for when a user logs-in through facebook
    //  If the user does not exist, then we create a new User in the database.
    private void checkUserExists(FirebaseUser user) {
        final FirebaseUser us = user;
        // Check if the user exists (for facebook)
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(us.getUid()).exists()) {
                    return;
                } else {
                    // Make new user account
                    String uid = us.getUid();
                    Account newAcc = new Account("", true);

                    // Send to database
                    mDatabase.child("Users").child(uid).setValue(newAcc);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    // Clears the textviews so as to not persist
    private void clearViews() {
        mEmailEditTextView.setText("");
        mPasswordEditTextView.setText("");
    }

    // TODO: Change login button color when its disabled

    // TODO: do I get all the stuff from database here before moving on or do I do it within next activity that I go to?
}

