package com.example.arutr.randomusers.screen.users;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.example.arutr.randomusers.UsersApp;
import com.example.arutr.randomusers.model.User;
import com.squareup.picasso.Picasso;


/**
 * @author Artur Romasiuk
 */
public final class Images {

    public static final String WIDTH_185 = "w185";
    public static final String WIDTH_780 = "w780";

    private Images() {
    }

    public static void loadUser(@NonNull ImageView imageView, @NonNull User user,
                                @NonNull String size) {
        loadUser(imageView, user.getPicture().getLarge(), size);
    }

    public static void loadUser(@NonNull ImageView imageView, @NonNull String posterPath,
                                @NonNull String size) {
        String url = posterPath;
        Picasso.with(imageView.getContext())
                .load(url)
                .noFade()
                .into(imageView);
    }

    public static void fetch(@NonNull String posterPath, @NonNull String size) {
        String url = posterPath;
        Picasso.with(UsersApp.getAppContext())
                .load(url)
                .fetch();
    }
}
