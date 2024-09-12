package com.aountech.heartdiseasesprediction.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.ui.activities.CardioPredictActivity;
import com.aountech.heartdiseasesprediction.ui.activities.TipsActivity;

public class HomeFragment extends Fragment {
    Activity context;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MaterialCardView card1 = context.findViewById(R.id.home_card_healthyTips);
        MaterialCardView card2 = context.findViewById(R.id.home_card_cardioCheck);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TipsActivity.class));
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "any thing ", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, CardioPredictActivity.class));
            }
        });

    }
}