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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signInEmailEditText, signInPasswordEditText;
    private TextView signUpTextView;
    private Button signInButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign in Activity");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signInEmailEditText=findViewById(R.id.signInEmailEditTextId);
        signInPasswordEditText=findViewById(R.id.signInPasswordEditTextId);
        signInButton=findViewById(R.id.signInButtonId);
        signUpTextView=findViewById(R.id.signUpTextViewId);

        progressBar=findViewById(R.id.progressbarId);

        // Listener Add
        signInButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signInButtonId:
                userLogin();
                break;
            case R.id.signUpTextViewId:
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);

                break;
        }
    }

    private void userLogin() {
        String email=signInEmailEditText.getText().toString().trim();
        String password=signInPasswordEditText.getText().toString().trim();

        // Email Validity
        if (email.isEmpty())
        {
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();
            return;
        }
        // Password Validity
        if (password.isEmpty())
        {
            signInPasswordEditText.setError("Enter a password");
            signInPasswordEditText.requestFocus();
            return;
        }
        if (password.length()<6)
        {
            signInPasswordEditText.setError("Minimum length of Password should be 6");
            signInPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Login in FIREBASE
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
            if (task.isSuccessful())
            {
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
