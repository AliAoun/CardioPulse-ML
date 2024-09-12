package com.aountech.heartdiseasesprediction.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.models.User;
import com.aountech.heartdiseasesprediction.utilities.Constants;

import java.util.ArrayList;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText et_email;
    Button btn_send;
    ImageView iv_back;
    ArrayList<User> users ;
    private String email;
    private String userID;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        et_email=findViewById(R.id.forgetPass_et_email);
        btn_send=findViewById(R.id.forgetPass_btn_send);
        iv_back=findViewById(R.id.forgetPass_iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPasswordActivity.this,SignInActivity.class));
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (et_email.getText().toString().isEmpty()){
                   Toast.makeText(ForgetPasswordActivity.this, "Enter Your Registered Email !", Toast.LENGTH_SHORT).show();
               }
               else {
                   email=et_email.getText().toString();
                   getUserID(email);
               }
            }
        });

    }

    private void getUserID(String email){
        firestore = FirebaseFirestore.getInstance();
        firestore.collection(Constants.KEY_COLLECTION_USERS).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.password = queryDocumentSnapshot.getString(Constants.KEY_PASSWORD);
                            user.id = queryDocumentSnapshot.getReference().getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            for (User user : users) {
                                if (user.email.equals(email)) {
                                    userID = user.id;
                                }
                            }
                            if (userID==null){
                                Toast.makeText(ForgetPasswordActivity.this, "User Not Founded", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(ForgetPasswordActivity.this,ResetPasswordActivity.class);
                                intent.putExtra("id",userID);
                                startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(getBaseContext(), "No Registered Uses Yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}