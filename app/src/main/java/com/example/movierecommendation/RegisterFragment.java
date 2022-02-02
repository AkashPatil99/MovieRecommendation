package com.example.movierecommendation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {
    private EditText username,mobileNumber,password;
    private Button login,register;
    private RegisterFragmentListener registerFragmentListener;

    public interface RegisterFragmentListener{
        void updateFragment();
        void addNewUser(User user);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login_btn);
        register = view.findViewById(R.id.register_btn);
        username = view.findViewById(R.id.username);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFragmentListener.updateFragment();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumberString = mobileNumber.getText().toString();
                String passwordString = password.getText().toString();
                String userName = username.getText().toString();
                if(userName.isEmpty()){
                    username.setError("user name can't empty");
                }
                else if(mobileNumberString.isEmpty()){
                    mobileNumber.setError("Mobile Number can't empty");
                }
                else if(passwordString.isEmpty()){
                    password.setError("Password can't empty");
                }
                else {
                    User user = new User(mobileNumberString,userName,passwordString);
                    registerFragmentListener.addNewUser(user);
                    registerFragmentListener.updateFragment();
                }
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegisterFragmentListener){
            registerFragmentListener = (RegisterFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registerFragmentListener = null;
    }
}
