package com.aountech.heartdiseasesprediction.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.ui.activities.SignInActivity;
import com.aountech.heartdiseasesprediction.utilities.Constants;
import com.aountech.heartdiseasesprediction.utilities.PreferenceManager;

import java.util.HashMap;

public class SettingsFragment extends Fragment {
    private PreferenceManager preferenceManager;
    Context context;
    Resources resources;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferenceManager = new PreferenceManager(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ImageView iv_profile = v.findViewById(R.id.settings_iv_imgProfile);
        ConstraintLayout ll_goToProfile =v.findViewById(R.id.settings_cl_editProfile);
        TextView tv_logout =v.findViewById(R.id.settings_tv_logout);
        TextView tv_userName =v.findViewById(R.id.settings_tv_userName);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_view);
        ImageView iv_darkMode = v.findViewById(R.id.settings_iv_darkMode);
        TextView tv_language = v.findViewById(R.id.settings_tv_language);
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        iv_profile.setImageBitmap(bitmap);

        tv_userName.setText(preferenceManager.getString(Constants.KEY_NAME));


        tv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showBottomDialog();
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        ll_goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragmentContainer, fragment);
                bottomNavigationView.setSelectedItemId(R.id.bottom_menu_profile);
                fragmentTransaction.commit();
            }
        });

        iv_darkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//                int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
//                iv_darkMode.setImageResource(R.drawable.ic_theme_dark);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        return v;

    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        LinearLayout arabic = dialog.findViewById(R.id.bottomSheet_ll_arabic);
        LinearLayout english = dialog.findViewById(R.id.bottomSheet_ll_english);
        ImageView cancelButton = dialog.findViewById(R.id.bottomSheet_iv_close);

        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                dialog.dismiss();
//                Toast.makeText(getActivity(),"arabic is clicked",Toast.LENGTH_SHORT).show();
//                context = LocaleHelper.setLocale(getActivity(), "hi");
//                resources = context.getResources();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                dialog.dismiss();
//                Toast.makeText(getActivity(),"english is Clicked",Toast.LENGTH_SHORT).show();
//                context = LocaleHelper.setLocale(getActivity(), "en");
//                resources = context.getResources();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

//    private void signOut(){
//        Toast.makeText(getActivity(), "Signing Out...", Toast.LENGTH_SHORT).show();
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference =database.collection(Constants.KEY_COLLECTION_USERS).document(
//                preferenceManager.getString(Constants.KEY_USER_ID)
//        );
//        HashMap<String,Object> updates = new HashMap<>();
//        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
//        documentReference.update(updates)
//                .addOnSuccessListener(end ->{
//                    preferenceManager.clear();
//                    startActivity(new Intent(getActivity(), SignInActivity.class));
//                })
//                .addOnFailureListener(e ->{
//                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
//                });
//    }

    private void signOut() {
        Toast.makeText(getActivity(), "Signing Out...", Toast.LENGTH_SHORT).show();

        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        // 1. Check if the user document exists first
        FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, proceed with update
                        DocumentReference documentReference = documentSnapshot.getReference();
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                        documentReference.update(updates)
                                .addOnSuccessListener(end -> {
                                    // Clear local storage (SharedPreferences)
                                    preferenceManager.clear();

                                    // Clear any other local storage mechanisms you might have (e.g., custom caching)

                                    // Clear user ID from secure storage (optional)
                                    // You can implement secure storage using libraries like Android Keystore

                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Unable To Sign Out", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Document doesn't exist, handle the case (optional)
                        Log.w("SignOut", "User document not found: " + userId);
                        preferenceManager.clear();
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error fetching user document", Toast.LENGTH_SHORT).show();
                });
    }


}