package com.aountech.heartdiseasesprediction.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.ui.fragments.UserDataFragment;
import com.aountech.heartdiseasesprediction.databinding.ActivityResultBinding;
import com.aountech.heartdiseasesprediction.models.Symptoms;
import com.aountech.heartdiseasesprediction.utilities.Constants;
import com.aountech.heartdiseasesprediction.utilities.PreferenceManager;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity implements UserDataFragment.OnFragmentClickListner {
    ActivityResultBinding binding;
    ProgressBar progressBar;
    public static Symptoms data;
    public static String result;

    public static String otherUserName;
    public static String otherUserEmail;
    Dialog dialog;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        Intent intent = getIntent();
        result = intent.getStringExtra("result");
        int r = Integer.parseInt(result);
        data =(Symptoms) intent.getSerializableExtra("inputs");
        if (r==1){
            binding.resultTvResult.setTextColor(Color.parseColor("#C62828"));
            binding.resultTvResult.setText(R.string.result_have);
//            Toast.makeText(getBaseContext(), "r==111111111", Toast.LENGTH_LONG).show();

        }
        else if (r==0){
            binding.resultTvResult.setTextColor(Color.parseColor("#4CAF50"));
            binding.resultTvResult.setText(R.string.result_notHave);
//            Toast.makeText(getBaseContext(), "r==0000000000000", Toast.LENGTH_LONG).show();
        }
//        binding.resultTvResult.setText(result+"");
        binding.resultBtnSaveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarLoading(true);
                showResultDialog("Save Related To This Account Data","Save Related To Another User Data");
                progressBarLoading(false);
            }
        });
        binding.resultIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this,CardioPredictActivity.class));
            }
        });
    }
    private void showResultDialog(String s1 , String s2) {

        dialog = new Dialog(ResultActivity.this);

        dialog.setContentView(R.layout.result_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.resultDialog_iv_close);
        TextView tv1 = dialog.findViewById(R.id.resultDialog_tv1);
        TextView tv2 = dialog.findViewById(R.id.resultDialog_tv2);

        tv1.setText(s1);
        tv2.setText(s2);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarLoading(true);
                dialog.cancel();
                saveInputsToThisAccount(data);
//                Toast.makeText(ResultActivity.this, preferenceManager.getString(Constants.KEY_USER_ID)+"", Toast.LENGTH_SHORT).show();
                progressBarLoading(false);
                Toast.makeText(getBaseContext(), "inputs saved successfully ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ResultActivity.this,CardioPredictActivity.class));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarLoading(true);
                dialog.cancel();

                UserDataFragment fragment = new UserDataFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                bundle.putSerializable("inputs" , data);
                fragment.setArguments(bundle);
                ft.replace(R.id.userData_fragmentContainer,fragment);
                View fragment1 =findViewById(R.id.userData_fragmentContainer);
                fragment1.setVisibility(View.VISIBLE);
                ft.commit();

//                Toast.makeText(ResultActivity.this, preferenceManager.getString(Constants.KEY_USER_ID)+"", Toast.LENGTH_SHORT).show();
                progressBarLoading(false);
//                Toast.makeText(getBaseContext(), "inputs saved successfully ", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(ResultActivity.this,PredictActivity.class));
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

//    private void saveInputsToAnotherAccount(Symptoms inputs){
//        progressBarLoading(true);
//        FirebaseFirestore database =FirebaseFirestore.getInstance();
//        Toast.makeText(this, "name: "+otherUserName+"\nemail: "+otherUserEmail, Toast.LENGTH_SHORT).show();
//        HashMap<String,Object> data = new HashMap<>();
//        data.put(Constants.KEY_USER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
//        data.put(Constants.KEY_NAME,otherUserName);
//        data.put(Constants.KEY_EMAIL,otherUserEmail);
//        data.put(Constants.KEY_AGE,inputs.getAge());
//        data.put(Constants.KEY_GENDER,inputs.getGender());
//        data.put(Constants.KEY_CHEST_PAIN_TYPE,inputs.getChestPain());
//        data.put(Constants.KEY_RESTING_BLOOD_PRESSURE,inputs.getRestingBloodPressure());
//        data.put(Constants.KEY_CHOLESTEROL,inputs.getCholesterol());
//        data.put(Constants.KEY_FASTING_BLOOD_SUGER,inputs.getFastingBloodSugar());
//        data.put(Constants.KEY_REST_ECG,inputs.getRestEcg());
//        data.put(Constants.KEY_MAX_HEART_RATE,inputs.getMaxHeartRate());
//        data.put(Constants.KEY_EXERCISE_INDUCED_ANGINA,inputs.getExerciseInducedAngina());
//        data.put(Constants.KEY_ST_DEPRESSION,inputs.getStDepression());
//        data.put(Constants.KEY_ST_SLOPE,inputs.getStSlope());
//        data.put(Constants.KEY_CA,inputs.getCa());
//        data.put(Constants.KEY_THAL,inputs.getThal());
//        data.put(Constants.KEY_RESULT,result);
//        database.collection(Constants.KEY_COLLECTION_USERS_DATA)
//                .add(data)
//                .addOnSuccessListener(documentReference -> {
//                    progressBarLoading(false);
////                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
////                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
////                    preferenceManager.putString(Constants.KEY_NAME,userName);
//                    //
////                    preferenceManager.putString(Constants.KEY_EMAIL,binding.signUpEtEmail.getText().toString());
//                    //
////                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
////                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                    startActivity(intent);
//
//
//                })
//                .addOnFailureListener(exception->{
//                    progressBarLoading(false);
//                    Toast.makeText(this, exception.getMessage()+"", Toast.LENGTH_SHORT).show();;
//                });
//    }
    private void saveInputsToThisAccount(Symptoms inputs){
        progressBarLoading(true);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        HashMap<String,Object> data = new HashMap<>();
        data.put(Constants.KEY_USER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
        data.put(Constants.KEY_NAME,preferenceManager.getString(Constants.KEY_NAME));
        data.put(Constants.KEY_EMAIL,preferenceManager.getString(Constants.KEY_EMAIL));
        data.put(Constants.KEY_AGE,inputs.getAge());
        data.put(Constants.KEY_GENDER,inputs.getGender());
        data.put(Constants.KEY_CHEST_PAIN_TYPE,inputs.getChestPain());
        data.put(Constants.KEY_RESTING_BLOOD_PRESSURE,inputs.getRestingBloodPressure());
        data.put(Constants.KEY_CHOLESTEROL,inputs.getCholesterol());
        data.put(Constants.KEY_FASTING_BLOOD_SUGAR,inputs.getFastingBloodSugar());
        data.put(Constants.KEY_REST_ECG,inputs.getRestEcg());
        data.put(Constants.KEY_MAX_HEART_RATE,inputs.getMaxHeartRate());
        data.put(Constants.KEY_EXERCISE_INDUCED_ANGINA,inputs.getExerciseInducedAngina());
        data.put(Constants.KEY_ST_DEPRESSION,inputs.getStDepression());
        data.put(Constants.KEY_ST_SLOPE,inputs.getStSlope());
        data.put(Constants.KEY_CA,inputs.getCa());
        data.put(Constants.KEY_THAL,inputs.getThal());
        data.put(Constants.KEY_RESULT,result);
        database.collection(Constants.KEY_COLLECTION_USERS_DATA)
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    progressBarLoading(false);
//                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
//                    preferenceManager.putString(Constants.KEY_NAME,userName);
                    //
//                    preferenceManager.putString(Constants.KEY_EMAIL,binding.signUpEtEmail.getText().toString());
                    //
//                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
//                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);


                })
                .addOnFailureListener(exception->{
                    progressBarLoading(false);
                    Toast.makeText(this, exception.getMessage()+"", Toast.LENGTH_SHORT).show();;
                });
    }


    public void progressBarLoading(boolean isLoading){
        View view =getLayoutInflater().inflate(R.layout.result_dialog_layout,null);
        progressBar = view.findViewById(R.id.resultDialog_progressBar);
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSaveClicked(String userName, String userEmail) {
        otherUserName= userName;
        otherUserEmail= userEmail;
    }
}