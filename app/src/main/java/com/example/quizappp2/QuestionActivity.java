package com.example.quizappp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizappp2.Adapters.QuestionsAdapter;
import com.example.quizappp2.Models.QuestionModel;
import com.example.quizappp2.databinding.ActivityQuestionBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ActivityQuestionBinding binding;
    FirebaseDatabase database;
    ArrayList<QuestionModel> list;
    QuestionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        // Safely retrieve setNum and categoryName
        int setNum = getIntent().getIntExtra("setNum", -1);
        String categoryName = getIntent().getStringExtra("categoryName");


        if (setNum == -1 || categoryName == null) {
            Toast.makeText(this, "Error: Missing required data.", Toast.LENGTH_SHORT).show();
            finish(); // Close Activity if required data is missing
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyQuestions.setLayoutManager(layoutManager);

        adapter = new QuestionsAdapter(this, list, categoryName, new QuestionsAdapter.DeleteListener() {
            @Override
            public void onLongClick(int position, String id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setTitle("Delete question");
                builder.setMessage("Are you sure you want to delete this question?");
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    database.getReference().child("Sets").child(categoryName).child("questions")
                            .child(id).removeValue().addOnSuccessListener(unused -> Toast.makeText(QuestionActivity.this, "Question deleted", Toast.LENGTH_SHORT).show());
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        binding.recyQuestions.setAdapter(adapter);

        database.getReference().child("Sets").child(categoryName).child("questions")
                .orderByChild("setNum").equalTo(setNum)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                                model.setKey(dataSnapshot.getKey());
                                list.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(QuestionActivity.this, "No questions exist.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        binding.addQuestions.setOnClickListener(view -> {
            Intent intent = new Intent(QuestionActivity.this, AddQuestionActivity.class);
            intent.putExtra("category", categoryName);
            intent.putExtra("setNum", setNum);
            startActivity(intent);
        });

        binding.back.setOnClickListener(view -> onBackPressed());
    }
}
