package com.example.mobileapps2023;

import androidx.appcompat.app.AppCompatActivity;

public class User {
    public String userName;


    public String email;


    public static int ID = 1111;


    public User(){//default constuctor
        userName = " ";
        email = " ";
    }

    public User(String userName, String email){//alternate constructor
        this.userName = userName;
        this.email = email;
    }


    public int getID(){return ID;}



}
