package com.aountech.heartdiseasesprediction.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.aountech.heartdiseasesprediction.BuildConfig;
import com.aountech.heartdiseasesprediction.R;
import com.aountech.heartdiseasesprediction.models.User;
import com.aountech.heartdiseasesprediction.ui.fragments.FavoriteFragment;
import com.aountech.heartdiseasesprediction.ui.fragments.HomeFragment;
import com.aountech.heartdiseasesprediction.ui.fragments.ProfileFragment;
import com.aountech.heartdiseasesprediction.ui.fragments.SettingsFragment;
import com.aountech.heartdiseasesprediction.utilities.Constants;
import com.aountech.heartdiseasesprediction.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<User> users ;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
//    private FirebaseFirestore firestore;

//    private String docID;
    View header;
    TextView drawer_userName,drawer_userEmail ,main_tv_userName, main_toolbar_tvTitle;
    ImageView drawer_userImg, main_ic_logout ,main_iv_menuIcon, main_toolbar_imageView, main_tv_homeTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceManager = new PreferenceManager(getApplicationContext());
//        getDocId();
//        toolbar = findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);

//        main_tv_userName=findViewById(R.id.main_tv_name);
        main_iv_menuIcon=findViewById(R.id.home_iv_menu);
        main_toolbar_imageView =findViewById(R.id.main_toolbar_imageView);
        main_toolbar_tvTitle=findViewById(R.id.main_toolbar_textview);

        showToast(preferenceManager.getString(Constants.DOC_ID));

        drawerLayout= findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                //Closing drawer on item click
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_SHORT).show();
                        replaceFragment(new HomeFragment());
                        bottomNavigationView.setSelectedItemId(R.id.bottom_menu_home);
                        return true;
                    case R.id.nav_contact:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "abc@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                        startActivity(Intent.createChooser(emailIntent, null));
//                        Toast.makeText(getApplicationContext(), "Contact Selected", Toast.LENGTH_SHORT).show();
//                        replaceFragment(new ContactFragment());
                        return true;
                    case R.id.nav_share:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage= "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
//                        Toast.makeText(getApplicationContext(), "Share Selected", Toast.LENGTH_SHORT).show();
//                        replaceFragment(new ShareFragment());
                        return true;
                    case R.id.nav_settings:
                        Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
                        replaceFragment(new SettingsFragment());
                        bottomNavigationView.setSelectedItemId(R.id.bottom_menu_settings);
                        return true;
                    case R.id.nav_logout:
                        showToast("signing out ....");
                        signOut();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
            }
        }});
        header =navigationView.getHeaderView(0);
        drawer_userName=header.findViewById(R.id.drawer_tv_userName);
        drawer_userEmail=header.findViewById(R.id.drawer_tv_userEmail);
        drawer_userImg=header.findViewById(R.id.drawer_iv_userImg);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loadUserDetails();
        getToken();
        listenser();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();

        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setBackground(null);
        replaceFragment(new HomeFragment());
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_menu_home:
                    replaceFragment(new HomeFragment());
                    main_toolbar_tvTitle.setText("Home");
                    break;
                case R.id.bottom_menu_favorite:
                    replaceFragment(new FavoriteFragment());
                    main_toolbar_tvTitle.setText("Favorite");
                    break;
                case R.id.bottom_menu_profile:
                    replaceFragment(new ProfileFragment());
                    main_toolbar_tvTitle.setText("Profile");
                    break;
                case R.id.bottom_menu_settings:
                    replaceFragment(new SettingsFragment());
                    main_toolbar_tvTitle.setText("Settings");
                    break;
            }

            return true;
        });

    }

    private void listenser(){
        main_toolbar_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });
        main_iv_menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isOpen()){
                    drawerLayout.close();
                }
                else {
                    drawerLayout.open();
                }
            }
        });
    }

    private void loadUserDetails(){
        drawer_userName.setText(preferenceManager.getString(Constants.KEY_NAME));
        drawer_userEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        main_toolbar_imageView.setImageBitmap(bitmap);
        drawer_userImg.setImageBitmap(bitmap);
    }

    private void showToast(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
                .addOnSuccessListener(end -> showToast("Token Updated Successfully"))
                .addOnFailureListener(e -> showToast("Unable To Update Token"));
    }
    private void signOut(){
        showToast("Signing Out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(end ->{
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->{
                   showToast("Unable to Sign Out");
                });
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

//    private void getDocId(){
//        firestore = FirebaseFirestore.getInstance();
//        firestore.collection(Constants.KEY_COLLECTION_USERS)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult()!=null) {
//                        users = new ArrayList<>();
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                            User user = new User();
//                            user.id= queryDocumentSnapshot.getReference().getId();
//                            user.email= queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
//                            user.name= queryDocumentSnapshot.getString(Constants.KEY_NAME);
//                            user.password= queryDocumentSnapshot.getString(Constants.KEY_PASSWORD);
//                            user.image= queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
//                            users.add(user);
//                        }
//                        if (users.size()>0){
//                            for (User user :users){
//                                if (user.email.equals(preferenceManager.getString(Constants.KEY_EMAIL))){
//                                    docID=user.id;
//                                    preferenceManager.putString(Constants.DOC_ID,docID);
//                                    Toast.makeText(this, docID+"", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//    }
}


















