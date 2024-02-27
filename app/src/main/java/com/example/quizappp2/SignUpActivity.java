package com.example.quizappp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizappp2.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Creating your account");
        dialog.setMessage("Your account is creating");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                auth.createUserWithEmailAndPassword(binding.userEmail.getText().toString(),binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                dialog.dismiss();
                                if (task.isSuccessful()){

                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put("userName",binding.userName.getText().toString());
                                    map.put("userEmail",binding.userEmail.getText().toString());
                                    map.put("password",binding.password.getText().toString());
                                    map.put("profile","https://firebasestorage.googleapis.com/v0/b/quiz-admin-969cd.appspot.com/o/pp.png?alt=media&token=b0d75ba3-0813-4d41-8bfa-f173b4d27cd6");
                                    String id = task.getResult().getUser().getUid();

                                    database.getReference().child("admin_user").child(id).setValue(map);

                                    Intent intent = new Intent(SignUpActivity.this, com.example.quizappp2.MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else {

                                    Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });

        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, com.example.quizappp2.SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}