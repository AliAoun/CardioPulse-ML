package com.aountech.heartdiseasesprediction.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.databinding.ActivityCardioPredictBinding;
import com.aountech.heartdiseasesprediction.models.Symptoms;
import com.aountech.heartdiseasesprediction.utilities.PreferenceManager;
import com.aountech.heartdiseasesprediction.utilities.Values;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class CardioPredictActivity extends AppCompatActivity {
    private ActivityCardioPredictBinding binding;
    Toolbar toolbar;
    float[] inputValues = new float[13];
    private PreferenceManager preferenceManager;
    Interpreter tflite;
    private static Symptoms inputs;
    private static String age,gender,chestPain,restingBloodPressure,cholesterol,fastingBloodSugar,restEcg,maxHeartRate,exerciseInducedAngina,
            stDepression,stSlope,ca,thal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCardioPredictBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        toolbar =(Toolbar) findViewById(R.id.cardioPredict_toolbar);
//        setSupportActionBar(toolbar);
        preferenceManager = new PreferenceManager(getApplicationContext());

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception e){
            e.printStackTrace();
        }
        setAdapters();
        setListeners();
    }



    public float doInference(){
        //float[] inputVal = {90,1,3,118,182,0,0,174,0,0,2,0,2};
        //float[] inputVal ={90,1,3,150,243,1,0,137,1,1,2,0,1 };//1
//        float[] inputVal ={55,1,4,132,353,0,0,132,1,2,2,1,7}; //2

        //inputVal[0] = 54;
        float[][] outputval = new float[1][1];

        tflite.run(inputValues, outputval);

        float inferredValue = outputval[0][0];
        return inferredValue;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("new_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void setAdapters(){
        String[] genderValues = {"male","female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, genderValues);
        binding.cardioPredictTvGender.setAdapter(genderAdapter);

//        String[] chestPainValues = {"typical angina","atypical angina","non anginal pain","asymptotic"};
        String[] chestPainValues = {Values.CHESTPAIN_TYPICAL_ANGINA,Values.CHESTPAIN_ATYPICAL_ANGINA,Values.CHESTPAIN_NON_ANGINAL_PAIN,Values.CHESTPAIN_ASYMPTOTIC};
        ArrayAdapter<String> chestPainAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, chestPainValues);
        binding.cardioPredictTvChestPain.setAdapter(chestPainAdapter);

//        String [] fastingBloodSugarValues = {"not have fasting blood sugar","have fasting blood sugar"};
        String [] fastingBloodSugarValues = {Values.FASTING_BLOOD_NOT_HAVE,Values.FASTING_BLOOD_HAVE};
        ArrayAdapter<String> fastingBloodSugarAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fastingBloodSugarValues);
        binding.cardioPredictTvFastingBlood.setAdapter(fastingBloodSugarAdapter);

//        String [] restEcgvalues = {"normal","have st-t wave abnormality","left ventricular hyperthrophy"};
        String [] restEcgvalues = {Values.REST_ECG_NORMAL,Values.REST_ECG_ABNORMALITY,Values.REST_ECG_HYPERTHROPHY};
        ArrayAdapter<String> restEcgAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, restEcgvalues);
        binding.cardioPredictTvRestEcg.setAdapter(restEcgAdapter);

//        String [] exerciseInducedAnginaValues = {"not have induced angina","have induced angina"};
        String [] exerciseInducedAnginaValues = {Values.INDUCED_ANGINA_NOT_HAVE,Values.INDUCED_ANGINA_HAVE};
        ArrayAdapter<String> exerciseInducedAnginaAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, exerciseInducedAnginaValues);
        binding.cardioPredictTvExerciseInducedAngina.setAdapter(exerciseInducedAnginaAdapter);

//        String [] stSlopeValues = {"up sloping","flat","down sloping"};
        String [] stSlopeValues = {Values.ST_SLOPE_UP_SLOPING,Values.ST_SLOPE_FLAT,Values.ST_SLOPE_DOWN_SLOPING};
        ArrayAdapter<String> stSlopeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, stSlopeValues);
        binding.cardioPredictTvStSlope.setAdapter(stSlopeAdapter);

//        String [] thalValues = {"normal","fixed defect","reversable defect"};
        String [] thalValues = {Values.THAL_NORMAL,Values.THAL_FIXED_DEFECT,Values.THAL_REVERSABLE_DEFECT};
        ArrayAdapter<String> thalAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, thalValues);
        binding.cardioPredictTvThal.setAdapter(thalAdapter);

        String [] caValues = {"0","1","2","3"};
        ArrayAdapter<String> caAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, caValues);
        binding.cardioPredictTvCa.setAdapter(caAdapter);
    }

    private boolean validateData(){
        if (binding.cardioPredictEtAge.getText().toString().isEmpty()){
            binding.cardioPredictEtAge.setError("Age Required !");
            return false;
        }
        else if (binding.cardioPredictTvGender.getText().toString().isEmpty()){
//            binding.cardioPredictTvGender.setError("Gender Required !");
            showToast("Gender Required !");
            return false;
        }
        else if (binding.cardioPredictTvChestPain.getText().toString().isEmpty()){
//            binding.cardioPredictTvChestPain.setError("Chest Pain Type Required !");
            showToast("Chest Pain Type Required !");
            return false;
        }
        else if (binding.cardioPredictEtRestingBlood.getText().toString().isEmpty()){
            binding.cardioPredictEtRestingBlood.setError("Resting Blood Pressure Required !");
            return false;
        }
        else if (binding.cardioPredictEtCholesterol.getText().toString().isEmpty()){
            binding.cardioPredictEtCholesterol.setError("Cholesterol Required !");
            return false;
        }
        else if (binding.cardioPredictTvFastingBlood.getText().toString().isEmpty()){
//            binding.cardioPredictTvFastingBlood.setError("Fasting Blood Sugar Required !");
            showToast("Fasting Blood Sugar Required !");
            return false;
        }
        else if (binding.cardioPredictTvRestEcg.getText().toString().isEmpty()){
//            binding.cardioPredictTvRestEcg.setError("Rest ECG Required !");
            showToast("Rest ECG Required !");
            return false;
        }
        else if (binding.cardioPredictEtMaxHeartRate.getText().toString().isEmpty()){
            binding.cardioPredictEtMaxHeartRate.setError("Max Heart Rate Required !");
            return false;
        }
        else if (binding.cardioPredictTvExerciseInducedAngina.getText().toString().isEmpty()){
//            binding.cardioPredictTvExerciseInducedAngina.setError("Exercise Induced Angina Required !");
            showToast("Exercise Induced Angina Required !");
            return false;
        }
        else if (binding.cardioPredictEtStDepression.getText().toString().isEmpty()){
            binding.cardioPredictEtStDepression.setError("St_Depression Required !");
            return false;
        }
        else if (binding.cardioPredictTvStSlope.getText().toString().isEmpty()){
//            binding.cardioPredictTvStSlope.setError("St_Slope Required !");
            showToast("St_Slope Required !");
            return false;
        }
        else if (binding.cardioPredictTvCa.getText().toString().isEmpty()){
//            binding.cardioPredictTvCa.setError("Ca Value Required !");
            showToast("Ca Value Required !");
            return false;
        }
        else if (binding.cardioPredictTvThal.getText().toString().isEmpty()){
//            binding.cardioPredictTvThal.setError("Thal Value Required !");
            showToast("Thal Value Required !");
            return false;
        }
        else {
            return true;
        }
    }
    private boolean getInputs(){
        int a =Integer.parseInt(binding.cardioPredictEtAge.getText().toString());
        if (a>29&&a<79){
            binding.cardioPredictEtAge.setError(null);
            age=String.valueOf(a);
        }
        else {
            binding.cardioPredictEtAge.setError("Age must be 29:79 ");
        }
        String g= binding.cardioPredictTvGender.getText().toString();
        binding.cardioPredictEtAge.setError(null);
        if (g.equals(Values.GENDER_MALE)){
            gender="0";
            showToast(gender);
        }
        else if (g.equals(Values.GENDER_FEMALE)){
            gender="1";
            showToast(gender);
        }
        else {
            showToast(gender+""+g);
        }
        String cp = binding.cardioPredictTvChestPain.getText().toString();
        if (cp.equals(Values.CHESTPAIN_TYPICAL_ANGINA)){
            chestPain="0";
        }
        else if (cp.equals(Values.CHESTPAIN_ATYPICAL_ANGINA)){
            chestPain="1";
        }
        else if (cp.equals(Values.CHESTPAIN_NON_ANGINAL_PAIN)){
            chestPain="2";
        }
        else if (cp.equals(Values.CHESTPAIN_ASYMPTOTIC)){
            chestPain="3";
        }
        else {
            showToast("error getting chest pain value ");
        }
        int rbp = Integer.parseInt(binding.cardioPredictEtRestingBlood.getText().toString());
        if (rbp < 80 || rbp > 180){
            binding.cardioPredictEtRestingBlood.setError("RBP must be 80 : 180");
        }
        else {
            restingBloodPressure = String.valueOf(rbp);
        }
        int ch =Integer.parseInt(binding.cardioPredictEtCholesterol.getText().toString());
        if (ch<0 ||ch>600){
            binding.cardioPredictEtCholesterol.setError("Cholesterol must be in range 0:600");
        }
        else {
            cholesterol = String.valueOf(ch);
        }
        String fbs = binding.cardioPredictTvFastingBlood.getText().toString();
        if (fbs.equals(Values.FASTING_BLOOD_NOT_HAVE)){
            fastingBloodSugar="0";
        }
        else if (fbs.equals(Values.FASTING_BLOOD_HAVE)){
            fastingBloodSugar="1";
        }
        else {
            showToast("Error Getting Fast Blood Value !");
        }
        String recg = binding.cardioPredictTvRestEcg.getText().toString();
        if (recg.equals(Values.REST_ECG_NORMAL)){
            restEcg = "0";
        }
        else if (recg.equals(Values.REST_ECG_ABNORMALITY)){
            restEcg = "1";
        }
        else if (recg.equals(Values.REST_ECG_HYPERTHROPHY)){
            restEcg = "2";
        }
        else {
            showToast("Error Getting Rest ECG Value !");
        }
        int mhr = Integer.parseInt(binding.cardioPredictEtMaxHeartRate.getText().toString());
        if (mhr<80 || mhr>250){
            binding.cardioPredictEtMaxHeartRate.setError("Max Heart Rate Must Be In Range 80 : 250 !");
        }
        else {
            binding.cardioPredictEtAge.setError(null);
            maxHeartRate = String.valueOf(mhr);
        }
        String eia = binding.cardioPredictTvExerciseInducedAngina.getText().toString();
        if (eia.equals(Values.INDUCED_ANGINA_NOT_HAVE)){
            exerciseInducedAngina="0";
        }
        else if (eia.equals(Values.INDUCED_ANGINA_HAVE)){
            exerciseInducedAngina="1";
        }
        else {
            showToast("Error Getting Exercise Indued Angina Value !");
        }
        int depr = Integer.parseInt(binding.cardioPredictEtStDepression.getText().toString());
        if (depr<0 || depr>6){
            binding.cardioPredictEtStDepression.setError("St_Depression Must Be In Range 0:6 !");
        }
        else {
            stDepression = String.valueOf(depr);
        }
        String slop = binding.cardioPredictTvStSlope.getText().toString();
        if (slop.equals(Values.ST_SLOPE_UP_SLOPING)){
            stSlope="0";
        }
        else if (slop.equals(Values.ST_SLOPE_FLAT)){
            stSlope="1";
        }
        else if (slop.equals(Values.ST_SLOPE_DOWN_SLOPING)){
            stSlope="2";
        }
        else{
            showToast("Error Getting St_Slope Value !");
        }
        int c = Integer.parseInt(binding.cardioPredictTvCa.getText().toString());
        if (c<0 || c>3){
            binding.cardioPredictTvCa.setError("Ca Must Be In Range 0:3 !");
        }
        else {
            ca=String.valueOf(c);
        }
        String th = binding.cardioPredictTvThal.getText().toString();
        if (th.equals(Values.THAL_NORMAL)){
            thal="3";
        }
        else if (th.equals(Values.THAL_FIXED_DEFECT)){
            thal="6";
        }
        else if (th.equals(Values.THAL_REVERSABLE_DEFECT)){
            thal="7";
        }
        else {
            showToast("Error Getting Thal Value !");
        }
        inputValues[0]=Float.parseFloat(age);
        inputValues[1]=Float.parseFloat(gender);
        inputValues[2]=Float.parseFloat(chestPain);
        inputValues[3]=Float.parseFloat(restingBloodPressure);
        inputValues[4]=Float.parseFloat(cholesterol);
        inputValues[5]=Float.parseFloat(fastingBloodSugar);
        inputValues[6]=Float.parseFloat(restEcg);
        inputValues[7]=Float.parseFloat(maxHeartRate);
        inputValues[8]=Float.parseFloat(exerciseInducedAngina);
        inputValues[9]=Float.parseFloat(stDepression);
        inputValues[10]=Float.parseFloat(stSlope);
        inputValues[11]=Float.parseFloat(ca);
        inputValues[12]=Float.parseFloat(thal);

        inputs = new Symptoms(age,gender,chestPain,restingBloodPressure,cholesterol,fastingBloodSugar,restEcg,maxHeartRate,exerciseInducedAngina,stDepression,stSlope,ca,thal);

        return true;
    }


    private void setListeners(){
        binding.CardioPredictBtnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               if (getData()){
//                   showToast( "age : "+age+"\n"+
//                           "gender : "+gender+"\n"+
//                           "chestPain : "+chestPain+"\n"+
//                           "resting : "+restingBloodPressure+"\n"+
//                           "chol : "+cholesterol+"\n"+
//                           "fasting : "+fastingBloodSugar+"\n"+
//                           "restE : "+restEcg+"\n"+
//                           "maxheart : "+maxHeartRate+"\n"+
//                           "ex angina : "+exerciseInducedAngina+"\n"+
//                           "depress : "+stDepression+"\n"+
//                           "slope : "+stSlope+"\n"+
//                           "ca : "+ca+"\n"+
//                           "thal : "+thal
//                   );
//

                if (validateData()) {
                    if (getInputs()) {
                        progressBarLoading(true);
                        float prediction = doInference();
                        //                   showToast("age : "+age+"\n"+
                        //                        "gender : "+gender+"\n"+
                        //                        "chestPain : "+chestPain+"\n"+
                        //                        "resting : "+restingBloodPressure+"\n"+
                        //                        "chol : "+cholesterol+"\n"+
                        //                        "fasting : "+fastingBloodSugar+"\n"+
                        //                        "restE : "+restEcg+"\n"+
                        //                        "maxheart : "+maxHeartRate+"\n"+
                        //                        "ex angina : "+exerciseInducedAngina+"\n"+
                        //                        "depress : "+stDepression+"\n"+
                        //                        "slope : "+stSlope+"\n"+
                        //                        "ca : "+ca+"\n"+
                        //                        "thal : "+thal
                        //                   );

                        showToast(prediction + "");
                    Intent intent = new Intent(CardioPredictActivity.this,ResultActivity.class);
                    intent.putExtra("result",String.valueOf(prediction));
                    intent.putExtra("inputs",inputs);
                        progressBarLoading(false);
//                    startActivity(intent);

                    if (prediction >= 0.5) {
                        intent.putExtra("result",String.valueOf(1));
                        startActivity(intent);
                    } else if (prediction<0.5){
                        intent.putExtra("result",String.valueOf(0));
                        startActivity(intent);
                    }
                    else {
                        showToast("somthing wrong, check your inputs");
                    }
                    }
                }
            }
        });

        binding.cardioPredictIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardioPredictActivity.this,MainActivity.class));
            }
        });

        binding.cardioPredictIvChestPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "It should display the type of chest-pain experienced by the individual using the following format :\n" +
                        "0 = typical angina\n" +
                        "1 = atypical angina\n" +
                        "2 = non — anginal pain\n" +
                        "3 = asymptotic";
                infoDialog("Chest-pain type:",info);
            }
        });

        binding.cardioPredictIvGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "Choose one of the two radio buttons :\n" +
                        "0 = male\n" +
                        "1 = female\n";
                infoDialog("Gender :",info);
            }
        });

        binding.cardioPredictIvStSlope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "St_Slope types :\n" +
                        "0 = up sloping\n" +
                        "1 = flat\n"+
                        "2 = down sloping\n";
                infoDialog("St Slope :",info);
            }
        });

        binding.cardioPredictIvAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "Age must be from 29 to 77  :\n" ;
                infoDialog("Age :",info);
            }
        });

        binding.cardioPredictIvMaxHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "It should display the max heart rate achieved by an individual.";
                infoDialog("Max heart rate:",info);
            }
        });

        binding.cardioPredictIvExerciseInducedAngina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "Peak Exercise Induced Angina Value :\n" +
                        "0 = don't have exercise induced angina\n" +
                        "1 = have exercise induced angina\n";
                infoDialog("Exercise Induced Angina :",info);
            }
        });

        binding.cardioPredictIvRestEcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "It should display resting electrocardiographic results\n" +
                        "0 = normal\n" +
                        "1 = having ST-T wave abnormality\n" +
                        "2 = left ventricular hyperthrophy";
                infoDialog("Resting ECG:",info);
            }
        });

        binding.cardioPredictIvCholesterol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "It should display the serum cholesterol in mg/dl (unit)";
                infoDialog("Cholestrol:",info);
            }
        });

        binding.cardioPredictIvRestingBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "It should display the resting blood pressure value of an individual in mmHg (unit)";
                infoDialog("Resting Blood Pressure:",info);
            }
        });

        binding.cardioPredictIvFastingBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "It compares the fasting blood sugar value of an individual with 120mg/dl.\n" +
                        "If fasting blood sugar > 120mg/dl then : 1 (true)\n" +
                        "else : 0 (false)";
                infoDialog("Fasting Blood Sugar:",info);
            }
        });

        binding.cardioPredictIvStDepression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "ST depression induced by exercise relative to rest, should display the value which is an integer or float. Write zero (0) if nothing.";
                infoDialog("ST depression:",info);
            }
        });
        binding.cardioPredictIvCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "Ca, should display the value which is an integer in range of 0 : 3. Write zero (0) if nothing.";
                infoDialog("Ca :",info);
            }
        });
        binding.cardioPredictIvThal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = "Thal , should use one of these values \n" +
                        "1 = normal \n" +
                        "2 = fixed defect \n" +
                        "3 = reversable defect";
                infoDialog("Thal :",info);
            }
        });
    }

    private void infoDialog(String i, String string) {
        Dialog dialog;
        dialog = new Dialog(CardioPredictActivity.this);

        dialog.setContentView(R.layout.dialog_info_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.dialog_iv_close);
        TextView nameDialog = dialog.findViewById(R.id.dialog_tv_name);
        TextView infoDialog = dialog.findViewById(R.id.dialog_tv_info);

        nameDialog.setText(""+i);
        infoDialog.setText(string);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void progressBarLoading(boolean isLoading){
        if (isLoading){
            binding.CardioPredictBtnPredict.setEnabled(false);
            binding.cardioPredictProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.cardioPredictProgressBar.setVisibility(View.INVISIBLE);
            binding.CardioPredictBtnPredict.setEnabled(true);
        }
    }

    public void showToast(String s){
        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_SHORT).show();
    }

    //    private void getUserInputs(){
//        if (binding.cardioPredictEtAge.getText().toString().isEmpty()){
//            binding.cardioPredictEtAge.setError("Age Required !");
//        }
//        else {
//            int a =Integer.parseInt(binding.cardioPredictEtAge.getText().toString());
//            if (a>29&&a<79){
//                binding.cardioPredictEtAge.setError(null);
//                age=String.valueOf(a);
//            }
//            else {
//                binding.cardioPredictEtAge.setError("Age must be 29:79 ");
//            }
//        }
//        if (binding.cardioPredictTvGender.getText().toString().isEmpty()){
//            binding.cardioPredictTvGender.setError("Gender Required !");
//        }
//        else {
//            String g= binding.cardioPredictTvGender.getText().toString();
//            binding.cardioPredictEtAge.setError(null);
//            if (g.equals(Values.GENDER_MALE)){
//                gender="0";
//                showToast(gender);
//            }
//            else if (g.equals(Values.GENDER_FEMALE)){
//                gender="1";
//                showToast(gender);
//            }
//            else {
//                showToast(gender+""+g);
//            }
//        }
//        if (binding.cardioPredictTvChestPain.getText().toString().isEmpty()){
//            binding.cardioPredictTvChestPain.setError("Chest Pain Type Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            String cp = binding.cardioPredictTvChestPain.getText().toString();
//            if (cp.equals(Values.CHESTPAIN_TYPICAL_ANGINA)){
//                chestPain="0";
//            }
//            else if (cp.equals(Values.CHESTPAIN_ATYPICAL_ANGINA)){
//                chestPain="1";
//            }
//            else if (cp.equals(Values.CHESTPAIN_NON_ANGINAL_PAIN)){
//                chestPain="2";
//            }
//            else if (cp.equals(Values.CHESTPAIN_ASYMPTOTIC)){
//                chestPain="3";
//            }
//            else {
//                showToast("error getting chest pain value ");
//            }
//        }
//        if (binding.cardioPredictEtRestingBlood.getText().toString().isEmpty()){
//            binding.cardioPredictEtRestingBlood.setError("Resting Blood Pressure Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            int rbp = Integer.parseInt(binding.cardioPredictEtRestingBlood.getText().toString());
//            if (rbp < 80 || rbp > 180){
//                binding.cardioPredictEtRestingBlood.setError("RBP must be 80 : 180");
//            }
//            else {
//                restingBloodPressure = String.valueOf(rbp);
//            }
//        }
//        if (binding.cardioPredictEtCholesterol.getText().toString().isEmpty()){
//            binding.cardioPredictEtCholesterol.setError("Cholesterol Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            int c =Integer.parseInt(binding.cardioPredictEtCholesterol.getText().toString());
//            if (c<0 ||c>600){
//                binding.cardioPredictEtCholesterol.setError("Cholesterol must be in range 0:600");
//            }
//            else {
//                cholesterol = String.valueOf(c);
//            }
//        }
//        if (binding.cardioPredictTvFastingBlood.getText().toString().isEmpty()){
//            binding.cardioPredictTvFastingBlood.setError("Fasting Blood Sugar Required !");
//        }
//        else{
//            binding.cardioPredictEtAge.setError(null);
//            String fbs = binding.cardioPredictTvFastingBlood.getText().toString();
//            if (fbs.equals(Values.FASTING_BLOOD_NOT_HAVE)){
//                fastingBloodSugar="0";
//            }
//            else if (fbs.equals(Values.FASTING_BLOOD_HAVE)){
//                fastingBloodSugar="1";
//            }
//            else {
//                showToast("Error Getting Fast Blood Value !");
//            }
//        }
//        if (binding.cardioPredictTvRestEcg.getText().toString().isEmpty()){
//            binding.cardioPredictTvRestEcg.setError("Rest ECG Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            String recg = binding.cardioPredictTvRestEcg.getText().toString();
//            if (recg.equals(Values.REST_ECG_NORMAL)){
//                restEcg = "0";
//            }
//            else if (recg.equals(Values.REST_ECG_ABNORMALITY)){
//                restEcg = "1";
//            }
//            else if (recg.equals(Values.REST_ECG_HYPERTHROPHY)){
//                restEcg = "2";
//            }
//            else {
//                showToast("Error Getting Rest ECG Value !");
//            }
//        }
//        if (binding.cardioPredictEtMaxHeartRate.getText().toString().isEmpty()){
//            binding.cardioPredictEtMaxHeartRate.setError("Max Heart Rate Required !");
//        }
//        else {
//            int mhr = Integer.parseInt(binding.cardioPredictEtMaxHeartRate.getText().toString());
//            if (mhr<80 || mhr>250){
//                binding.cardioPredictEtMaxHeartRate.setError("Max Heart Rate Must Be In Range 80 : 250 !");
//            }
//            else {
//                binding.cardioPredictEtAge.setError(null);
//                maxHeartRate = String.valueOf(mhr);
//            }
//        }
//        if (binding.cardioPredictTvExerciseInducedAngina.getText().toString().isEmpty()){
//            binding.cardioPredictTvExerciseInducedAngina.setError("Exercise Induced Angina Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            String eia = binding.cardioPredictTvExerciseInducedAngina.getText().toString();
//            if (eia.equals(Values.INDUCED_ANGINA_NOT_HAVE)){
//                exerciseInducedAngina="0";
//            }
//            else if (eia.equals(Values.INDUCED_ANGINA_HAVE)){
//                exerciseInducedAngina="1";
//            }
//            else {
//                showToast("Error Getting Exercise Indued Angina Value !");
//            }
//        }
//        if (binding.cardioPredictEtStDepression.getText().toString().isEmpty()){
//            binding.cardioPredictEtStDepression.setError("St_Depression Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            int depr = Integer.parseInt(binding.cardioPredictEtStDepression.getText().toString());
//            if (depr<0 || depr>6){
//                binding.cardioPredictEtStDepression.setError("St_Depression Must Be In Range 0:6 !");
//            }
//            else {
//                stDepression = String.valueOf(depr);
//            }
//        }
//        if (binding.cardioPredictTvStSlope.getText().toString().isEmpty()){
//            binding.cardioPredictTvStSlope.setError("St_Slope Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            String slop = binding.cardioPredictTvStSlope.getText().toString();
//            if (slop.equals(Values.ST_SLOPE_UP_SLOPING)){
//                stSlope="0";
//            }
//            else if (slop.equals(Values.ST_SLOPE_FLAT)){
//                stSlope="1";
//            }
//            else if (slop.equals(Values.ST_SLOPE_DOWN_SLOPING)){
//                stSlope="2";
//            }
//            else{
//                showToast("Error Getting St_Slope Value !");
//            }
//        }
//
//        if (binding.cardioPredictTvCa.getText().toString().isEmpty()){
//            binding.cardioPredictTvCa.setError("Ca Value Required !");
//        }
//        else {
//            binding.cardioPredictEtAge.setError(null);
//            int c = Integer.parseInt(binding.cardioPredictTvCa.getText().toString());
//            if (c<0 || c>3){
//                binding.cardioPredictTvCa.setError("Ca Must Be In Range 0:3 !");
//            }
//            else {
//                ca=String.valueOf(c);
//            }
//        }
//
//        if (binding.cardioPredictTvThal.getText().toString().isEmpty()){
//            binding.cardioPredictTvThal.setError("Thal Value Required !");
//        }
//        else {
//            String th = binding.cardioPredictTvThal.getText().toString();
//            if (th.equals(Values.THAL_NORMAL)){
//                thal="3";
//            }
//            else if (th.equals(Values.THAL_FIXED_DEFECT)){
//                thal="6";
//            }
//            else if (th.equals(Values.THAL_REVERSABLE_DEFECT)){
//                thal="7";
//            }
//            else {
//                showToast("Error Getting Thal Value !");
//            }
//        }
//        showToast( "age : "+age+"\n"+
//                        "gender : "+gender+"\n"+
//                        "chestPain : "+chestPain+"\n"+
//                        "resting : "+restingBloodPressure+"\n"+
//                        "chol : "+cholesterol+"\n"+
//                        "fasting : "+fastingBloodSugar+"\n"+
//                        "restE : "+restEcg+"\n"+
//                        "maxheart : "+maxHeartRate+"\n"+
//                        "ex angina : "+exerciseInducedAngina+"\n"+
//                        "depress : "+stDepression+"\n"+
//                        "slope : "+stSlope+"\n"+
//                        "ca : "+ca+"\n"+
//                        "thal : "+thal
//        );
//
//    }

//    private boolean isAllInpusValid(){
//        if (binding.cardioPredictEtAge.getText().toString().trim().isEmpty()){
//            showToast("Enter Age");
//            return false;
//        }
//        else if (binding.cardioPredictTvGender.getText().toString().isEmpty()){
//            showToast("Choose Gender");
//            return false;
//        }
//        else if (binding.cardioPredictTvChestPain.getText().toString().isEmpty()){
//            showToast("Choose Chest Pain Value");
//            return false;
//        }
//        else if (binding.cardioPredictEtRestingBlood.getText().toString().trim().isEmpty()){
//            showToast("Enter Resting Blood Value");
//            return false;
//        }
//        else if (binding.cardioPredictEtCholesterol.getText().toString().trim().isEmpty()){
//            showToast("Enter Cholesterol Value");
//            return false;
//        }
//        else if (binding.cardioPredictTvFastingBlood.getText().toString().isEmpty()){
//            showToast("Choose Fasting Blood Sugar Value");
//            return false;
//        }
//        else if (binding.cardioPredictTvRestEcg.getText().toString().isEmpty()){
//            showToast("Choose Rest Ecg Value");
//            return false;
//        }
//        else if (binding.cardioPredictEtMaxHeartRate.getText().toString().trim().isEmpty()){
//            showToast("Enter Max Heart Rate Value");
//            return false;
//        }
//        else if (binding.cardioPredictTvExerciseInducedAngina.getText().toString().isEmpty()){
//            showToast("Choose Exercise Angina Value");
//            return false;
//        }
//        else if (binding.cardioPredictEtStDepression.getText().toString().trim().isEmpty()){
//            showToast("Enter St_Depression Value");
//            return false;
//        }
//        else if (binding.cardioPredictTvStSlope.getText().toString().isEmpty()){
//            showToast("St Slope Value");
//            return false;
//        }
//        else if (binding.cardioPredictTvCa.getText().toString().isEmpty()){
//            showToast("Choose Ca Value");
//            return false;
//        }
//        else if (binding.cardioPredictTvThal.getText().toString().isEmpty()){
//            showToast("Choose Thal Value");
//            return false;
//        }
//        else {
//            getInputValues();
//            return true;
//        }
//    }

//    private void getInputValues(){
//        int a=Integer.parseInt(binding.cardioPredictEtAge.getText().toString());
//        if ( a > 77 && a <29){
//            binding.cardioPredictEtAge.setError("age must be 29 : 77 ");
//        }
//        else {
//            age = String.valueOf(a);
//        }
//
//        binding.cardioPredictTvGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                gender=parent.getSelectedItem().toString();
//            }
//        });
//
//        binding.cardioPredictTvChestPain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                chestPain=String.valueOf(position);
//            }
//        });
//
//        int rbp = Integer.parseInt(binding.cardioPredictEtRestingBlood.getText().toString().trim());
//        if (rbp < 80 && rbp>180){
//            showToast("Resting Blood Pressure Must Be In Range 80 : 180 ");
//        }
//        else {
//            restingBloodPressure =String.valueOf(rbp);
//        }
//
//        int chol = Integer.parseInt(binding.cardioPredictEtCholesterol.getText().toString().trim());
//        if (chol<0 && chol>600){
//            showToast("Cholesterol Value Must Be In Range 0 : 600 ");
//        }
//        else {
//            cholesterol =String.valueOf(chol);
//        }
//
//        binding.cardioPredictTvFastingBlood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                fastingBloodSugar=String.valueOf(position);
//            }
//        });
//
//        binding.cardioPredictTvRestEcg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                restEcg = String.valueOf(position);
//            }
//        });
//
//        int mhr = Integer.parseInt(binding.cardioPredictEtMaxHeartRate.getText().toString().trim());
//        if (mhr<80 && mhr>250){
//            showToast("Max Heart Rate Must Be In Range 80 :250 ");
//        }
//        else {
//            maxHeartRate = String.valueOf(mhr);
//        }
//
//
//        binding.cardioPredictTvExerciseInducedAngina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                exerciseInducedAngina= String.valueOf(position);
//            }
//        });
//
//        int depr = Integer.parseInt(binding.cardioPredictEtStDepression.getText().toString());
//        if (depr<0 && depr>6){
//            showToast("St Depression Must Be In Range 0 : 6 ");
//        }
//        else {
//            stDepression = String.valueOf(depr);
//        }
//
//        binding.cardioPredictTvStSlope.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                stSlope=String.valueOf(position);
//            }
//        });
//
//        binding.cardioPredictTvCa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ca=String.valueOf(position);
//            }
//        });
//
//        binding.cardioPredictTvThal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position==0){
//                    thal="3";
//                }
//                else if (position==1){
//                    thal="6";
//                }
//                else if (position==3){
//                    thal="7";
//                }
//                else{
//                    showToast("erorr getting thal value !");
//                }
//            }
//        });


//        inputValues[0]=Float.parseFloat(age);
//        inputValues[1]=Float.parseFloat(gender);
//        inputValues[2]=Float.parseFloat(chestPain);
//        inputValues[3]=Float.parseFloat(restingBloodPressure);
//        inputValues[4]=Float.parseFloat(cholesterol);
//        inputValues[5]=Float.parseFloat(fastingBloodSugar);
//        inputValues[6]=Float.parseFloat(restEcg);
//        inputValues[7]=Float.parseFloat(maxHeartRate);
//        inputValues[8]=Float.parseFloat(exerciseInducedAngina);
//        inputValues[9]=Float.parseFloat(stDepression);
//        inputValues[10]=Float.parseFloat(stSlope);
//        inputValues[11]=Float.parseFloat(ca);
//        inputValues[12]=Float.parseFloat(thal);

//        inputs = new Symptoms(age,gender,chestPain,restingBloodPressure,cholesterol,fastingBloodSugar,restEcg,maxHeartRate,exerciseInducedAngina,stDepression,stSlope,ca,thal);
//        Toast.makeText(this, "age : "+age+"\n"+
//                        "gender : "+gender+"\n"+
//                        "chestPain : "+chestPain+"\n"+
//                        "resting : "+restingBloodPressure+"\n"+
//                        "chol : "+cholesterol+"\n"+
//                        "fasting : "+fastingBloodSugar+"\n"+
//                        "restE : "+restEcg+"\n"+
//                        "maxheart : "+maxHeartRate+"\n"+
//                        "ex angina : "+exerciseInducedAngina+"\n"+
//                        "depress : "+stDepression+"\n"+
//                        "slope : "+stSlope+"\n"+
//                        "ca : "+ca+"\n"+
//                        "thal : "+thal
//                , Toast.LENGTH_SHORT).show();
//    }
}