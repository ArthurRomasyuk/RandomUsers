package com.example.arutr.randomusers.network;

import com.example.arutr.randomusers.model.UserResponse;
import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Artur Romasiuk
 */

public interface UserService {

    @GET("api/?results=50")
    Observable<UserResponse> allUsers();
}
