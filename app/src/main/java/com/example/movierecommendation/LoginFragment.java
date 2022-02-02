package com.example.movierecommendation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    private EditText mobileNumber,password;
    private Button login,register;
    private CheckBox checkBox;
    private LoginFragmentListener loginFragmentListener;

    public interface LoginFragmentListener{
        void updateFragment();
        void checkCredentials(String mobileNumber,String password,int check);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login_btn);
        register = view.findViewById(R.id.register_btn);
        checkBox = view.findViewById(R.id.checkbox);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragmentListener.updateFragment();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumberString = mobileNumber.getText().toString();
                String passwordString = password.getText().toString();

                if(mobileNumberString.isEmpty()){
                    mobileNumber.setError("Mobile Number can't empty");
                }
                else if(passwordString.isEmpty()){
                    password.setError("Password can't empty");
                }
                else {
                    if(checkBox.isChecked())
                        loginFragmentListener.checkCredentials(mobileNumberString,passwordString,1);
                    else
                        loginFragmentListener.checkCredentials(mobileNumberString,passwordString,0);
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginFragmentListener){
            loginFragmentListener = (LoginFragmentListener) context;
        }
        else{
            //exception
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginFragmentListener = null;
    }
}
