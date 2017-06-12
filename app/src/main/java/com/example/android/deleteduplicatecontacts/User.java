package com.example.android.deleteduplicatecontacts;

/**
 * Created by subhankar on 12/6/17.
 */

public class User {
    String name, number;

    public User(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
