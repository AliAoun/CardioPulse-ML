package com.chatapp.chatapp.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.chatapp.chatapp.databinding.ActivitySignUpBinding;
import com.chatapp.chatapp.utilities.Constants;
import com.chatapp.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        listeners();
    }
    private void listeners(){
        binding.signUpTvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSignUpDetailsIsValid()){
                    signUp();
                }
            }
        });
        binding.signUpFlImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                pickAnImage.launch(intent);
            }
        });
    }
    private void signUp(){
        progressBarLoading(true);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.signUpEtName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.signUpEtEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.signUpEtPassword.getText().toString());
        user.put(Constants.KEY_IMAGE,encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    progressBarLoading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.signUpEtName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception->{
                    progressBarLoading(false);
                    showToast(exception.getMessage());
                });
    }
    private String getEncodedImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight = bitmap.getHeight()*previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickAnImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()==RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.signInIvProfileImage.setImageBitmap(bitmap);
                            binding.signUpTvAddImage.setVisibility(View.GONE);
                            encodedImage = getEncodedImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    private boolean isSignUpDetailsIsValid(){
        if (encodedImage==null){
            showToast("Select Profile Image");
            return false;
        }
        else if (binding.signUpEtName.getText().toString().trim().isEmpty()){
            showToast("Enter Name");
            return false;
        }
        else if (binding.signUpEtEmail.getText().toString().trim().isEmpty()){
            showToast("Enter Email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.signUpEtEmail.getText().toString()).matches()){
            showToast("Enter Valid Email");
            return false;
        }
        else if (binding.signUpEtPassword.getText().toString().trim().isEmpty()){
            showToast("Enter Password");
            return false;
        }
        else if (binding.signUpEtConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Enter Confirm Password");
            return false;
        }
        else if (!binding.signUpEtPassword.getText().toString().equals(binding.signUpEtConfirmPassword.getText().toString())){
            showToast("Password & Confirm Password Must Be Same ");
        }
        else {
            return true;
        }
        return false;
    }

    public void showToast(String s){
        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_SHORT).show();
    }

    public void progressBarLoading(boolean isLoading){
        if (isLoading){
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.signUpProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.signUpProgressBar.setVisibility(View.INVISIBLE);
            binding.btnSignUp.setVisibility(View.VISIBLE);
        }
    }

}