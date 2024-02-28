package com.example.quizappp2.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizappp2.Adapters.CategoryAdapter;
import com.example.quizappp2.Models.CategoryModel;
//import com.example.quizappp2.Models.UserModel;
import com.example.quizappp2.R;
import com.example.quizappp2.SignInActivity;
import com.example.quizappp2.databinding.FragmentHomeBinding;
import com.example.quizappp2.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseDatabase database;
    ArrayList<CategoryModel> list;
    CategoryAdapter adapter;
    ProgressDialog progressDialog;
    Dialog loadingDialog;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_dialog);

        if (loadingDialog.getWindow()!=null){

            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(true);

        }

        loadingDialog.show();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.recyCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(getContext(),list);
        binding.recyCategory.setAdapter(adapter);

        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        CategoryModel post= dataSnapshot.getValue(CategoryModel.class);
                        post.setKey(dataSnapshot.getKey());
                        list.add(post);

                    }
                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();


                }
                else {

                    Toast.makeText(getContext(), "Category not exist", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}