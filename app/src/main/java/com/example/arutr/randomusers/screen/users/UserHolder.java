package com.example.arutr.randomusers.screen.users;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.arutr.randomusers.R;
import com.example.arutr.randomusers.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Artur Romasiuk
 */
public class UserHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    ImageView mImageView;

    @NonNull
    public static UserHolder create(@NonNull Context context, int imageHeight, int imageWidth) {
        View view = View.inflate(context, R.layout.image_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = imageHeight;
        layoutParams.width = imageWidth;
        imageView.requestLayout();
        return new UserHolder(view);
    }

    private UserHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull User user) {
        Images.loadUser(mImageView, user, Images.WIDTH_185);
    }
}
