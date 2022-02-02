package com.example.movierecommendation;

public class User {
    private String username;
    private String mobileNumber;
    public String password;

    public User(String mobileNumber,String username,String password){
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
