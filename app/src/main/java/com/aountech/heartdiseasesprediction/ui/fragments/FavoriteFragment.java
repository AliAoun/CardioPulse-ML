package com.aountech.heartdiseasesprediction.ui.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.adapters.SavedDataAdapter;
import com.aountech.heartdiseasesprediction.models.Patient;
import com.aountech.heartdiseasesprediction.utilities.Constants;
import com.aountech.heartdiseasesprediction.utilities.OnRvItemClickListner;
import com.aountech.heartdiseasesprediction.utilities.PreferenceManager;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements OnRvItemClickListner {
    RecyclerView recyclerView ;
    FirebaseFirestore firestore;
    Dialog dialog;
    PreferenceManager preferenceManager;
    SavedDataAdapter adapter;
    ArrayList<Patient> patients;
    ProgressBar progressBar;
    Patient returnedPatient;
//    SavedDataAdapter adapter;
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = v.findViewById(R.id.favorite_recyclerView);
        progressBar = v.findViewById(R.id.favorite_progressBar);
        getData();
        return v;
    }

    private void getData(){
        firestore.collection(Constants.KEY_COLLECTION_USERS_DATA).get()
                .addOnCompleteListener(task -> {
//                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult()!=null){
                        progressBarLoading(true);
                        patients = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot :task.getResult()){
//                            if (currentUserId.equals(queryDocumentSnapshot.getId())){
//                                continue;
//                            }
                            Patient patient = new Patient();
                            patient.userName=queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            patient.userEmail=queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            patient.age=queryDocumentSnapshot.getString(Constants.KEY_AGE);
                            patient.gender=queryDocumentSnapshot.getString(Constants.KEY_GENDER);
                            patient.chestPain=queryDocumentSnapshot.getString(Constants.KEY_CHEST_PAIN_TYPE);
                            patient.restingBloodPressure=queryDocumentSnapshot.getString(Constants.KEY_RESTING_BLOOD_PRESSURE);
                            patient.cholesterol=queryDocumentSnapshot.getString(Constants.KEY_CHOLESTEROL);
                            patient.fastingBloodSugar=queryDocumentSnapshot.getString(Constants.KEY_FASTING_BLOOD_SUGAR);
                            patient.restEcg=queryDocumentSnapshot.getString(Constants.KEY_REST_ECG);
                            patient.maxHeartRate=queryDocumentSnapshot.getString(Constants.KEY_MAX_HEART_RATE);
                            patient.exerciseInducedAngina=queryDocumentSnapshot.getString(Constants.KEY_EXERCISE_INDUCED_ANGINA);
                            patient.stDepression=queryDocumentSnapshot.getString(Constants.KEY_ST_DEPRESSION);
                            patient.stSlope=queryDocumentSnapshot.getString(Constants.KEY_ST_SLOPE);
                            patient.ca=queryDocumentSnapshot.getString(Constants.KEY_CA);
                            patient.thal=queryDocumentSnapshot.getString(Constants.KEY_THAL);
                            patient.result=queryDocumentSnapshot.getString(Constants.KEY_RESULT);
                            patient.userId=queryDocumentSnapshot.getString(Constants.KEY_USER_ID);
                            patient.docId=queryDocumentSnapshot.getReference().getId();
                            patients.add(patient);
                        }
                        progressBarLoading(false);
                            if (patients.size()>0){
                                adapter = new SavedDataAdapter(getActivity(), patients,this);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(adapter);
//                                Toast.makeText(getActivity(), patients.get(0).getResult()+"", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "No Patient Founded ", Toast.LENGTH_SHORT).show();
                            }

                    }
                    else {
                        Toast.makeText(getActivity(), "Task Is Not Successful !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    @Override
//    public void onItemClicked(Patient patient) {
//        showDialog(patient);
//    }

    @Override
    public void onItemClicked(Patient patient, int position) {
        showDialog(patient,position);
    }


    private void showDialog(Patient patient, int position) {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.show_patient_data_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.patientData_iv_close);
        TextView name = dialog.findViewById(R.id.patientData_name);
        TextView email = dialog.findViewById(R.id.patientData_email);
        TextView gender = dialog.findViewById(R.id.patientData_gender);
        TextView cp = dialog.findViewById(R.id.patientData_chestPain);
        TextView rbp = dialog.findViewById(R.id.patientData_restingBlood);
        TextView chol = dialog.findViewById(R.id.patientData_cholesterol);
        TextView fbs = dialog.findViewById(R.id.patientData_fastingBlood);
        TextView recg = dialog.findViewById(R.id.patientData_restEcg);
        TextView mhr = dialog.findViewById(R.id.patientData_maxHeartRate);
        TextView eia = dialog.findViewById(R.id.patientData_inducedAngina);
        TextView std = dialog.findViewById(R.id.patientData_stDepression);
        TextView sts = dialog.findViewById(R.id.patientData_stSlope);
        TextView ca = dialog.findViewById(R.id.patientData_ca);
        TextView th = dialog.findViewById(R.id.patientData_thal);
        TextView r = dialog.findViewById(R.id.patientData_result);
        Button btn_delete = dialog.findViewById(R.id.patientData_btn_delete);

        name.setText(patient.getUserName());
        email.setText(patient.getUserEmail());
        gender.setText(patient.getGender());
        cp.setText(patient.getChestPain());
        rbp.setText(patient.getRestingBloodPressure());
        chol.setText(patient.getCholesterol());
        fbs.setText(patient.getFastingBloodSugar());
        recg.setText(patient.getRestEcg());
        mhr.setText(patient.getMaxHeartRate());
        eia.setText(patient.getExerciseInducedAngina());
        std.setText(patient.getStDepression());
        sts.setText(patient.getStSlope());
        ca.setText(patient.getCa());
        th.setText(patient.getThal());
        r.setText(patient.getResult());

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePatient(patient);
                patients.remove(position);
                adapter.notifyItemRemoved(position);
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

    private void deletePatient(Patient patient){
        firestore = FirebaseFirestore.getInstance();
//        firestore.collection(Constants.KEY_COLLECTION_USERS_DATA).document()
//                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                DocumentReference dr = firestore.collection(Constants.KEY_COLLECTION_USERS_DATA).document(patient.getDocId());
                dr.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Success Delete", Toast.LENGTH_SHORT).show();
                            if (dialog.isShowing()) {
                                dialog.cancel();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Error Deleting", Toast.LENGTH_SHORT).show();
                            if (dialog.isShowing()){
                                dialog.cancel();
                            }

                        }
                    }
                });

    }
    public void progressBarLoading(boolean isLoading){
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}