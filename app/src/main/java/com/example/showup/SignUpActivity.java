package com.example.showup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpEmailEditText, signUpPasswordEditText;
    private TextView signInTextView;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign up Activity");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Progressbar
        progressBar=findViewById(R.id.progressbarId);

        signUpEmailEditText=findViewById(R.id.signUpEmailEditTextId);
        signUpPasswordEditText=findViewById(R.id.signUpPasswordEditTextId);
        signUpButton=findViewById(R.id.signUpButtonId);
        signInTextView=findViewById(R.id.signInTextViewId);

        // Listener Add
        signUpButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signUpButtonId:
                userRegister();
                break;
            case R.id.signInTextViewId:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                break;
        }
    }

    private void userRegister() {
        String email=signUpEmailEditText.getText().toString().trim();
        String password=signUpPasswordEditText.getText().toString().trim();

        // Email Validity
        if (email.isEmpty())
        {
            signUpEmailEditText.setError("Enter an email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpEmailEditText.setError("Enter an email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        // Password Validity
        if (password.isEmpty())
        {
            signUpPasswordEditText.setError("Enter a password");
            signUpPasswordEditText.requestFocus();
            return;
        }
        if (password.length()<6)
        {
            signUpPasswordEditText.setError("Minimum length of Password should be 6");
            signUpPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(), "Registration Successful and goto profile page", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                } else {
                    // If sign in fails, display a message to the user.
                    //Toast.makeText(getApplicationContext(), "Registration not Successful", Toast.LENGTH_SHORT).show();
                    if (task.getException()instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(), "User is already Registered", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
