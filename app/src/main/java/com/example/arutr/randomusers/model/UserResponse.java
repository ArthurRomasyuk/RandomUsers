package com.example.arutr.randomusers.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artur Romasiuk
 */

public class UserResponse {

    @SerializedName("results")
    private List<User> users;

    public List<User> getUsers() {
        if (users == null) {
            return new ArrayList<>();
        }
        return users;
    }
}
