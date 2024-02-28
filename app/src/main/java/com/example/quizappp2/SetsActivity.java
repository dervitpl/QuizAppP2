package com.example.quizappp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizappp2.Adapters.GrideAdapter;
import com.example.quizappp2.databinding.ActivitySetsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SetsActivity extends AppCompatActivity {

    ActivitySetsBinding binding;
    FirebaseDatabase database;

    String category;
    int sets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        category = getIntent().getStringExtra("category");
        sets = getIntent().getIntExtra("sets", 0);

        // Initialize the adapter before any anonymous class or lambda expression
        GrideAdapter adapter = new GrideAdapter(sets, category, new GrideAdapter.GridListener() {
            @Override
            public void addSets() {
                // Your existing implementation
            }

            @Override
            public void onSetSelected(int setNum) {
                // Launch QuestionActivity with the selected set number
                Intent intent = new Intent(SetsActivity.this, QuestionActivity.class);
                intent.putExtra("categoryName", category);
                intent.putExtra("setNum", setNum);
                startActivity(intent);
            }
        });

        binding.gridView.setAdapter(adapter);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
