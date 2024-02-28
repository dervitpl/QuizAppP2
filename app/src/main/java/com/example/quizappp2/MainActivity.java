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

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<CategoryModel> list;
    Dialog dialog;
    EditText inputCategoryName;
    Button uploadCategory;
    View fetchImage;
    CircleImageView categoryImage;
    Uri imageUri;
    ProgressDialog progressDialog;
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
        progressDialog.setMessage("Please wait...");

        uploadCategory = dialog.findViewById(R.id.btnUpload);
        inputCategoryName = dialog.findViewById(R.id.inputCategoryName);
        fetchImage = dialog.findViewById(R.id.fetchImage);
        categoryImage = dialog.findViewById(R.id.categoryImage);

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                binding.toolBarText.setText("Choose Category");
                binding.addCategory.setVisibility(View.VISIBLE);
                fragment = new HomeFragment();
            } else if (itemId == R.id.navigation_user) {
                binding.toolBarText.setText("User");
                binding.addCategory.setVisibility(View.GONE);
                fragment = new UserFragment();
            } else if (itemId == R.id.navigation_profile) {
                binding.toolBarText.setText("Profile");
                binding.addCategory.setVisibility(View.GONE);
                fragment = new ProfileFragment();
            } else {
                // This else block can be removed if you're sure all IDs are covered above
                return false;
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
            return false;
        });

        binding.addCategory.setOnClickListener(view -> dialog.show());

        fetchImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 7);
        });

        uploadCategory.setOnClickListener(view -> {
            String name = inputCategoryName.getText().toString();
            if (imageUri == null) {
                Toast.makeText(MainActivity.this, "Please upload category image", Toast.LENGTH_SHORT).show();
            } else if (name.isEmpty()) {
                inputCategoryName.setError("Enter category name");
            } else {
                progressDialog.show();
                uploadData();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==7){
            if (data!=null){

                imageUri=data.getData();
                categoryImage.setImageURI(imageUri);

            }
        }


    }

    private void uploadData() {

        final StorageReference reference= storage.getReference().child("category").child(new Date().getTime()+"");

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(MainActivity.this, "image Uploaded", Toast.LENGTH_SHORT).show();

                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setCategoryName(inputCategoryName.getText().toString());
                        categoryModel.setSetNum(0);
                        categoryModel.setCategoryImage(uri.toString());

                        database.getReference().child("categories")
                                .push()
                                .setValue(categoryModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
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
                });

            }
        });


    }


}