package com.aountech.heartdiseasesprediction.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.models.User;
import com.aountech.heartdiseasesprediction.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    Button btn_reset;
    EditText et_pass , et_confirmPass;
    ImageView iv_back;
    ArrayList<User> users ;
    private String email;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        btn_reset = findViewById(R.id.resetPass_btn_create);
        et_pass=findViewById(R.id.resetPass_et_pass);
        et_confirmPass=findViewById(R.id.resetPass_et_confirmPass);
        iv_back=findViewById(R.id.resetPass_iv_back);

        Intent i =getIntent();
        id=i.getStringExtra("id").toString();
        Toast.makeText(this, "id : "+id, Toast.LENGTH_SHORT).show();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this,ForgetPasswordActivity.class));
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pass.getText().toString().isEmpty()||et_confirmPass.getText().toString().isEmpty()){
                    Toast.makeText(ResetPasswordActivity.this, "enter passwords !", Toast.LENGTH_SHORT).show();
                }
                else {
                    if ((et_pass.getText().toString()).equals(et_confirmPass.getText().toString())){
                        String pass = et_pass.getText().toString();
                        resetPass(pass,id);
                    }else {
                        Toast.makeText(ResetPasswordActivity.this, "passwords must be the same !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void resetPass(String pass,String userID){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(userID);
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.KEY_PASSWORD, pass);
        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ResetPasswordActivity.this, "reset success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPasswordActivity.this,SignInActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResetPasswordActivity.this, "reset failed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPasswordActivity.this,ForgetPasswordActivity.class));
            }
        });
    }
}