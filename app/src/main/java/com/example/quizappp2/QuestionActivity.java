package com.example.quizappp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizappp2.databinding.ActivityQuestionBinding;

public class QuestionActivity extends AppCompatActivity {


    ActivityQuestionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot("setNum", 0));


        int setNum = getIntent().getIntExtra("setNum", 0);
        String categoryName = getIntent().getStringExtra("categoryName");

        binding.addQuestions.SetOnClickListener(new View.OnClickListener()) {
            @Override
                    public void onClick(View view) {
                    Intent intent = new Intent(QuestionActivity.this,AddQuestionActivity.class);
                    intent.putExtra("category",categoryName);
                    intent.putExtra("setNum", setNum);
                    startActivity(intent);

            }
        }
    }
}