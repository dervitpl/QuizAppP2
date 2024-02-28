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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizappp2.Models.UserModel;
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

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Uri imageUri;
    FirebaseStorage storage;
    FirebaseUser user;
    ProgressDialog dialog;
    Dialog dialog1;

    public ProfileFragment() {
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

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        user = auth.getCurrentUser();

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Profile Uploading");
        dialog.setMessage("We are uploading your profile");


        dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.contact_dialog);


        if (dialog1.getWindow() !=null){

            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.setCancelable(true);
        }


        database.getReference().child("admin_user").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    UserModel model = snapshot.getValue(UserModel.class);

                    binding.userName.setText(model.getUserName());
                    binding.emil.setText(model.getUserEmail());

                    Picasso.get()
                            .load(model.getProfile())
                            .placeholder(R.drawable.placeholder)
                            .into(binding.userProfile);

                }
                else {

                    Toast.makeText(getContext(), "data not exist", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        binding.fetchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,5);

            }
        });







        binding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog1.show();

            }
        });

        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String shareBody = "Patrz jaka super aplikacja do quizow";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(intent);

            }
        });


        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.signOut();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);

            }
        });


        return binding.getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==5){

            if (data!=null){

                imageUri = data.getData();
                binding.userProfile.setImageURI(imageUri);

                dialog.show();

                final StorageReference reference = storage.getReference().child("profile").child(user.getUid());

                reference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                dialog.dismiss();
                                reference.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {


                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("profile",uri.toString());

                                                database.getReference().child("admin_user").child(auth.getUid())
                                                        .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){

                                                                    Toast.makeText(getContext(), "Profile uploaded", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else {

                                                                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });




                                            }
                                        });

                            }
                        });

            }

        }



    }


}