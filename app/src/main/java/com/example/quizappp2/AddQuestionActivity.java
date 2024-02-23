package com.example.quizappp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.quizappp2.Models.QuestionModel;
import com.example.quizappp2.databinding.ActivityAddQuestionBinding;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity {


    ActivityAddQuestionBinding binding;
    int set;
    String categoryName;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        set = getIntent().getIntExtra("setNum", -1);
        categoryName = getIntent().getStringExtra("category");

        database = FirebaseDatabase.getInstance();
        if (set==-1) {
            finish();
            return;
        }

        binding.btnUploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int correct = -1;

                for (int i=0; i<binding.optionContainer.getChildCount();i++){
                    EditText answer = (EditText) binding.answerContainer.getChildAt(i);

                    if(answer.getText().toString().isEmpty()){

                        answer.setError(("Required"));
                        return;
                    }


                    RadioButton radioButton = (RadioButton) binding.optionContainer.getChildAt(1);
                    
                    if (radioButton.isChecked()){
                        
                        correct = 1;
                        break;
                    }


                }
                
                if (correct==-1) {
                    Toast.makeText(AddQuestionActivity.this, "please mark the correct option", Toast.LENGTH_SHORT).show();
                    return;
                }

                QuestionModel model = new QuestionModel();
                model.setQuestion(binding.inputQuestion.getText().toString());
                model.setOptionA(((EditText)binding.optionContainer.getChildAt(0)).getText().toString();
                model.setOptionB(((EditText)binding.optionContainer.getChildAt(0)).getText().toString();
                model.setOptionC(((EditText)binding.optionContainer.getChildAt(0)).getText().toString();
                model.setOptionD(((EditText)binding.optionContainer.getChildAt(0)).getText().toString();
            }
        });
    }
}



















