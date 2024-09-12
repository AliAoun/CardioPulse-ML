package com.aountech.heartdiseasesprediction.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.ui.activities.SignInActivity;
import com.aountech.heartdiseasesprediction.utilities.Constants;
import com.aountech.heartdiseasesprediction.utilities.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileFragment extends Fragment {
    private PreferenceManager preferenceManager;
    ImageView iv_profile;
    ImageView profile_iv_selectImage;
    FirebaseFirestore firestore;
    private String encodedImage;
    FrameLayout fl_userImage;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferenceManager = new PreferenceManager(getActivity());
        View v =inflater.inflate(R.layout.fragment_profile, container, false);
        TextView tv_userName =v.findViewById(R.id.profile_tv_name);
        TextView tv_userEmail =v.findViewById(R.id.profile_tv_email);
        iv_profile = v.findViewById(R.id.profile_iv_user);
        fl_userImage = v.findViewById(R.id.profile_fl_userImage);
        profile_iv_selectImage=v.findViewById(R.id.profile_iv_selectImage);
        TextView tv_logOut = v.findViewById(R.id.profile_tv_logout);
        tv_userName.setText(preferenceManager.getString(Constants.KEY_NAME));
        tv_userEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
            byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        iv_profile.setImageBitmap(bitmap);

        profile_iv_selectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    pickAnImage.launch(intent);
                    if (encodedImage!=null){
                        updatePicture();
                    }
                }
        });
        tv_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return v;
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
                if (result.getResultCode()==getActivity().RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            iv_profile.setImageBitmap(bitmap);
//                            binding.signUpTvAddImage.setVisibility(View.GONE);
                            encodedImage = getEncodedImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void updatePicture(){
        firestore=FirebaseFirestore.getInstance();
        DocumentReference docRef =firestore
                .collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.DOC_ID));
        HashMap<String,Object> map = new HashMap<>();
        map.put(Constants.KEY_IMAGE,encodedImage);
        docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Toast.makeText(getActivity(), "Image Updated ", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), preferenceManager.getString(Constants.DOC_ID)+"", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error Updating ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void signOut(){
        Toast.makeText(getActivity(), "Signing Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(end ->{
                    preferenceManager.clear();
                    startActivity(new Intent(getActivity(),SignInActivity.class));
                })
                .addOnFailureListener(e ->{
                    Toast.makeText(getActivity(), "Unable To Sign Out", Toast.LENGTH_SHORT).show();
                });
    }

}