package com.example.movierecommendation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener,RegisterFragment.RegisterFragmentListener{

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private DatabaseHandler databaseHandler;
    SharedPreferences credentials;
    public static final String MyCREDENTIALS = "User";
    public static final String STATUS = "Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        credentials = getSharedPreferences(MyCREDENTIALS,MODE_PRIVATE);

        if(credentials.contains(STATUS)){
            Intent loginIntent = new Intent(MainActivity.this,ListActivity.class);
            Toast.makeText(MainActivity.this,"LogIn automatic",Toast.LENGTH_SHORT).show();
            startActivity(loginIntent);
            finish();
        }

        databaseHandler = new DatabaseHandler(this);

        final Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            final FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
            final String loginFragmentName = LoginFragment.class.getName();
            final Fragment loginFragment = fragmentFactory.instantiate(getClassLoader(), loginFragmentName);

            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragmentContainer, loginFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void updateFragment() {
        final Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if(fragment instanceof LoginFragment){
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final RegisterFragment registerFragment = new RegisterFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, registerFragment);
            fragmentTransaction.commit();
        }
        else if (fragment instanceof RegisterFragment){
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final LoginFragment loginFragment = new LoginFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, loginFragment);
            fragmentTransaction.commit();
        }
        else {
            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void checkCredentials(String mobileNumber, String password,int check) {
        List<User> userList = databaseHandler.getAllUsers();

        for(int i=0;i<userList.size();i++){
            if(userList.get(i).getMobileNumber().equals(mobileNumber) && userList.get(i).getPassword().equals(password)){
                Toast.makeText(MainActivity.this,"Hello "+userList.get(i).getUsername(),Toast.LENGTH_LONG).show();

                if(check == 1){
                    SharedPreferences.Editor editor = credentials.edit();
                    editor.putString(STATUS,"true").apply();
                }

                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
                finish();

                return;
            }
        }
        Toast.makeText(MainActivity.this,"Check credentials",Toast.LENGTH_LONG).show();
    }

    @Override
    public void addNewUser(User user) {
        databaseHandler.addUser(user);
        Toast.makeText(MainActivity.this,"Registration Successfully",Toast.LENGTH_LONG).show();
    }
}