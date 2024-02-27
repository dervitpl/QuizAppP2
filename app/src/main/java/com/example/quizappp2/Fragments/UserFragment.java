package com.example.quizappp2.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizappp2.Adapters.UserAdapter;
import com.example.quizappp2.Models.UserModel;
import com.example.quizappp2.R;
import com.example.quizappp2.databinding.FragmentUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserFragment extends Fragment {

    FragmentUserBinding binding;
    UserAdapter adapters;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<UserModel> list = new ArrayList<>();
    public UserFragment() {
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
        binding = FragmentUserBinding.inflate(inflater, container, false);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        adapters = new UserAdapter(list, getContext());
        binding.rvUser.setAdapter(adapters);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rvUser.setLayoutManager(manager);


        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    UserModel users = dataSnapshot.getValue(UserModel.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){

                        list.add(users);
                    }

                }
                adapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}