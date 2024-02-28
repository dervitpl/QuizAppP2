package com.example.quizappp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.quizappp2.Fragments.HomeFragment;
import com.example.quizappp2.Fragments.ProfileFragment;
import com.example.quizappp2.Fragments.UserFragment;
import com.example.quizappp2.Models.CategoryModel;
import com.example.quizappp2.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<CategoryModel> list;
    Dialog dialog;
    EditText inputCategoryName;
    Button uploadCategory;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_category_dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        uploadCategory = dialog.findViewById(R.id.btnUpload);
        inputCategoryName = dialog.findViewById(R.id.inputCategoryName);

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                binding.toolBarText.setText("Choose Category");
                fragment = new HomeFragment();
            } else if (itemId == R.id.navigation_user) {
                binding.toolBarText.setText("User");
                fragment = new UserFragment();
            } else if (itemId == R.id.navigation_profile) {
                binding.toolBarText.setText("Profile");
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
            return true;
        });

        binding.addCategory.setOnClickListener(view -> dialog.show());

        uploadCategory.setOnClickListener(view -> {
            String name = inputCategoryName.getText().toString().trim();
            if (name.isEmpty()) {
                inputCategoryName.setError("Enter category name");
                return;
            }
            progressDialog.show();

            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setCategoryName(name);
            categoryModel.setSetNum(0);
            categoryModel.setCategoryImage(""); // Using an empty string for the image

            database.getReference().child("categories")
                    .push()
                    .setValue(categoryModel)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(MainActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    });
        });
    }
}
