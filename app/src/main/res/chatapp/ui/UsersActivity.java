 package com.chatapp.chatapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.chatapp.chatapp.adapters.UserAdapter;
import com.chatapp.chatapp.databinding.ActivityUsersBinding;
import com.chatapp.chatapp.models.User;
import com.chatapp.chatapp.utilities.Constants;
import com.chatapp.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

 public class UsersActivity extends AppCompatActivity {
    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding =ActivityUsersBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        listeners();
        getUsers();
    }
    private void listeners(){
        binding.usersIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

     public void progressBarLoading(boolean isLoading){
         if (isLoading){
             binding.usersProgressBar.setVisibility(View.VISIBLE);
         }
         else {
             binding.usersProgressBar.setVisibility(View.VISIBLE);
         }
     }

     private void getUsers(){
        progressBarLoading(true);
         FirebaseFirestore database = FirebaseFirestore.getInstance();
         database.collection(Constants.KEY_COLLECTION_USERS)
                 .get()
                 .addOnCompleteListener(task->{
                     progressBarLoading(false);
                     String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                     if (task.isSuccessful() && task.getResult() != null){
                         List<User> users = new ArrayList<>();
                         for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                             if (currentUserId.equals(queryDocumentSnapshot.getId())){
                                 continue;
                             }
                             User user = new User();
                             user.name=queryDocumentSnapshot.getString(Constants.KEY_NAME);
                             user.email=queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                             user.image=queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                             user.token=queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                             users.add(user);
                         }
                         if (users.size()>0){
                             UserAdapter userAdapter = new UserAdapter(users);
                             binding.usersRecyclerView.setAdapter(userAdapter);
                             binding.usersRecyclerView.setVisibility(View.VISIBLE);
                         }else {
                             showErrorMessage();
                         }
                     }else {
                         showErrorMessage();
                     }
                 });
     }

     private void showErrorMessage(){
        binding.usersTvErrorMessage.setText(String.format("5%","No User Available"));
        binding.usersTvErrorMessage.setVisibility(View.VISIBLE);
     }
}