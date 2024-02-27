package com.example.quizappp2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizappp2.Models.UserModel;
import com.example.quizappp2.R;
import com.example.quizappp2.databinding.UserRvDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{

    ArrayList<UserModel>list;
    Context context;

    public UserAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UserModel users = list.get(position);

        int userScore = users.getScore();

        if (userScore>=1){

            holder.binding.userName.setVisibility(View.VISIBLE);
            holder.binding.category.setVisibility(View.VISIBLE);
            holder.binding.score.setVisibility(View.VISIBLE);
            holder.binding.userProfiles.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(users.getProfile())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.userProfiles);

            holder.binding.userName.setText(users.getUserName());
            holder.binding.category.setText(users.getCategory());
            holder.binding.score.setText(users.getScore()+"");
            holder.binding.uId.setText(users.getUid());
        }
        else {

            holder.binding.userName.setVisibility(View.GONE);
            holder.binding.category.setVisibility(View.GONE);
            holder.binding.score.setVisibility(View.GONE);
            holder.binding.userProfiles.setVisibility(View.GONE);

        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder {
        UserRvDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding= UserRvDesignBinding.bind(itemView);
        }
    }
}
