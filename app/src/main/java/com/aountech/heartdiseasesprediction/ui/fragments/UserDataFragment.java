package com.aountech.heartdiseasesprediction.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.models.Symptoms;
import com.aountech.heartdiseasesprediction.ui.activities.CardioPredictActivity;
import com.aountech.heartdiseasesprediction.utilities.Constants;
import com.aountech.heartdiseasesprediction.utilities.PreferenceManager;

import java.util.HashMap;

public class UserDataFragment extends Fragment {
    PreferenceManager preferenceManager;
    OnFragmentClickListner listner;
    String userName;
    String userEmail;
    Symptoms inputs;
    String result;
    public UserDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferenceManager = new PreferenceManager(getActivity());
        inputs =(Symptoms) getArguments().getSerializable("inputs");
        result = (String)getArguments().getString("result");

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_user_data, container, false);
        EditText et_userName = v.findViewById(R.id.userData_et_userName);
        EditText et_userEmail = v.findViewById(R.id.userData_et_userEmail);
        Button btn_save = v.findViewById(R.id.userData_btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((et_userName.getText().toString().isEmpty())||(et_userEmail.getText().toString().isEmpty())){
                    Toast.makeText(getActivity(), "Fill User Data !", Toast.LENGTH_SHORT).show();
                }
                else {
                    userName = et_userName.getText().toString();
                    userEmail = et_userEmail.getText().toString();
//                    listner.onSaveClicked(userName,userEmail);
                    saveInputsToAnotherAccount(inputs);
                    Toast.makeText(getActivity(), "inputs saved successfully ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), CardioPredictActivity.class));

                }

            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentClickListner){
            listner= (OnFragmentClickListner) context;
        }
        else {
            throw new ClassCastException("Activity doesn't implements interface !!");
        }
    }

    private void saveInputsToAnotherAccount(Symptoms inputs){
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        Toast.makeText(getActivity(), "name: "+userName+"\nemail: "+userEmail, Toast.LENGTH_SHORT).show();
        HashMap<String,Object> data = new HashMap<>();
        data.put(Constants.KEY_USER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
        data.put(Constants.KEY_NAME,userName);
        data.put(Constants.KEY_EMAIL,userEmail);
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
                    Toast.makeText(getActivity(), exception.getMessage()+"", Toast.LENGTH_SHORT).show();;
                });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listner=null;
    }

    public interface OnFragmentClickListner{
        void onSaveClicked(String userName , String userEmail);
    }

}