package com.example.quizappp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizappp2.Adapters.CategoryAdapter;
import com.example.quizappp2.Fragments.HomeFragment;
import com.example.quizappp2.Fragments.ProfileFragment;
import com.example.quizappp2.Fragments.UserFragment;
import com.example.quizappp2.Models.CategoryModel;
import com.example.quizappp2.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<CategoryModel> list;
    CategoryAdapter adapter;
    Dialog dialog;
    EditText inputCategoryName;
    Button uploadCategory;
    CircleImageView categoryImage;
    Uri imageUri;
    ProgressDialog progressDialog;
    ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        list = new ArrayList<>();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_category_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("please wait");

        inputCategoryName = dialog.findViewById(R.id.inputCategoryName);
        uploadCategory = dialog.findViewById(R.id.btnUpload);
        categoryImage = dialog.findViewById(R.id.categoryImage);

        chipNavigationBar = findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.home) {
                    binding.toolBarText.setText("Choose Category");
                    binding.addCategory.setVisibility(View.VISIBLE);
                    fragment = new HomeFragment();
                } else if (i == R.id.user) {
                    binding.toolBarText.setText("User");
                    binding.addCategory.setVisibility(View.GONE);
                    fragment = new UserFragment();
                } else if (i == R.id.profile) {
                    binding.toolBarText.setText("Profile");
                    binding.addCategory.setVisibility(View.GONE);
                    fragment = new ProfileFragment();
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });

        binding.addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });
    }

    private void uploadData() {
        String name = inputCategoryName.getText().toString();
        if (name.isEmpty()) {
            inputCategoryName.setError("Enter category name");
            return;
        }

        progressDialog.show();
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCategoryName(inputCategoryName.getText().toString());
        categoryModel.setSetNum(0);

        database.getReference().child("categories")
                .push()
                .setValue(categoryModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                });
    }
                }


