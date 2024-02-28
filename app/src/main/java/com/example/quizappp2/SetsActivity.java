package com.example.quizappp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    GrideAdapter adapter;

    int a = 1;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        database = FirebaseDatabase.getInstance();
        String categoryKey = getIntent().getStringExtra("categoryKey");
        Log.d("SetsActivity", "Received categoryKey: " + categoryKey);
        String categoryName = getIntent().getStringExtra("categoryName");
        int sets = getIntent().getIntExtra("sets", 0);


        adapter = new GrideAdapter(sets, categoryName, categoryKey, new GrideAdapter.GridListener() {
            @Override
            public void addSets() {


                database.getReference().child("categories").child(categoryKey)
                        .child("setNum").setValue(getIntent().getIntExtra("sets",0)+a++).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()){

                                    adapter.sets++;
                                    adapter.notifyDataSetChanged();

                                }
                                else {

                                    Toast.makeText(SetsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


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