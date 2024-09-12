package com.aountech.heartdiseasesprediction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.models.Patient;
import com.aountech.heartdiseasesprediction.utilities.OnRvItemClickListner;

import java.util.ArrayList;

public class SavedDataAdapter extends RecyclerView.Adapter<SavedDataAdapter.SavedDataViewHolder> {
    ArrayList<Patient> patients;
    Context context;
    OnRvItemClickListner listner;

    public SavedDataAdapter(Context context , ArrayList<Patient> patients,OnRvItemClickListner listner) {
        this.context = context;
        this.patients = patients;
        this.listner = listner;
    }

    @NonNull
    @Override
    public SavedDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_returned_result_layout,parent,false);
        SavedDataViewHolder savedDataViewHolder = new SavedDataViewHolder(v);
        return savedDataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedDataViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.tv_name.setText(patient.getUserName());
        holder.tv_email.setText(patient.getUserEmail());
        holder.tv_result.setText(patient.getResult());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClicked(patient,patients.indexOf(patient));
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    class SavedDataViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name , tv_email , tv_result;
        CardView cardView;
        public SavedDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.savedResult_userName);
            tv_email = itemView.findViewById(R.id.savedResult_userEmail);
            tv_result = itemView.findViewById(R.id.savedResult_result);
            cardView = itemView.findViewById(R.id.savedResult_card);
        }
    }
}
