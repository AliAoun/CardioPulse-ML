package com.aountech.heartdiseasesprediction.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.adapters.TipsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TipsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<String> titles;
    List<String> infos;
    ImageView iv_back;
    TipsAdapter tipsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        recyclerView =findViewById(R.id.tips_recyclerView);
        iv_back=findViewById(R.id.tips_iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TipsActivity.this,MainActivity.class));
            }
        });
        titles = new ArrayList<>();
        infos = new ArrayList<>();
        titles.add("Control your blood pressure");
        titles.add("Keep your cholesterol and triglyceride levels under control");
        titles.add("Manage diabetes");
        titles.add("Stay at a healthy weight");
        titles.add("Eat a healthy diet");
        titles.add("Get regular exercise");
        titles.add("Limit alcohol");
        titles.add("Don't smoke");
        titles.add("Manage stress");
        titles.add("Make sure that you get enough sleep");

        infos.add("High BP is a major risk factor for heart disease. It is important to get your blood pressure checked regularly - at least once a year for most adults, and more often if you have high blood pressure. Take steps, including lifestyle changes, to prevent or control high blood pressure.");
        infos.add("High levels of cholesterol can clog your arteries and raise your risk of coronary artery disease and heart attack. Lifestyle changes and medicines (if needed) can lower your cholesterol. Triglycerides are another type of fat in the blood. High levels of triglycerides may also raise the risk of coronary artery disease, especially in women.\n"+"Total cholesterol level over 200\n" +
                "> HDL (\"good\") cholesterol level under 40\n" +
                "> LDL (\"bad\") cholesterol level over 160\n" +
                "> Triglycerides over 150");
        infos.add("Having diabetes doubles your risk of diabetic heart disease. That is because over time, high blood sugar from diabetes can damage your blood vessels and the nerves that control your heart and blood vessels. So, it is important to get tested for diabetes, and if you have it, to keep it under control.");
        infos.add("Being overweight or having obesity can increase your risk for heart disease. This is mostly because they are linked to other heart disease risk factors, including high blood cholesterol and triglyceride levels, high blood pressure, and diabetes. Controlling your weight can lower these risks.");
        infos.add("Try to limit saturated fats, foods high in sodium, and added sugars. Eat plenty of fresh fruit, vegetables, and whole grains. The DASH diet is an example of an eating plan that can help you to lower your blood pressure and cholesterol, two things that can lower your risk of heart disease.");
        infos.add("Exercise has many benefits, including strengthening your heart and improving your circulation. It can also help you maintain a healthy weight and lower cholesterol and blood pressure. All of these can lower your risk of heart disease.");
        infos.add("Drinking too much alcohol can raise your blood pressure. It also adds extra calories, which may cause weight gain. Both of those raise your risk of heart disease. Men should have no more than two alcoholic drinks per day, and women should not have more than one.");
        infos.add("Cigarette smoking raises your blood pressure and puts you at higher risk for heart attack and stroke. If you do not smoke, do not start. If you do smoke, quitting will lower your risk for heart disease. You can talk with your health care provider for help in finding the best way for you to quit.");
        infos.add("Stress is linked to heart disease in many ways. It can raise your blood pressure. Extreme stress can be a \"trigger\" for a heart attack. Also, some common ways of coping with stress, such as overeating, heavy drinking, and smoking, are bad for your heart. Some ways to help manage your stress include exercise, listening to music, focusing on something calm or peaceful, and meditating");
        infos.add("If you don't get enough sleep, you raise your risk of high blood pressure, obesity, and diabetes. Those three things can raise your risk for heart disease. Most adults need 7 to 9 hours of sleep per night. Make sure that you have good sleep habits. If you have frequent sleep problems, contact your health care provider. One problem, sleep apnea, causes people to briefly stop breathing many times during sleep. This interferes with your ability to get a good rest and can raise your risk of heart disease. If you think you might have it, ask your doctor about having a sleep study. And if you do have sleep apnea, make sure that you get treatment for it.");

        tipsAdapter = new TipsAdapter(this,titles,infos);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(tipsAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter((RecyclerView.Adapter) tipsAdapter);
    }
}