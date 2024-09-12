package com.chatapp.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chatapp.chatapp.databinding.UserItemContainerBinding;
import com.chatapp.chatapp.models.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemContainerBinding userItemContainerBinding = UserItemContainerBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new UserViewHolder(userItemContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder{
        UserItemContainerBinding binding;
        UserViewHolder(UserItemContainerBinding itemContainerBinding){
            super(itemContainerBinding.getRoot());
            binding = itemContainerBinding;
        }
        void setUserData(User user){
            binding.itemUserEmail.setText(user.email);
            binding.itemUserName.setText(user.name);
            binding.itemUserImage.setImageBitmap(getUserImage(user.image));
        }
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}




















