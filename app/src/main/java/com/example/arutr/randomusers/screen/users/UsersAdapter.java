package com.example.arutr.randomusers.screen.users;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.example.arutr.randomusers.model.User;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Artur Romasiuk
 */
public class UsersAdapter extends RecyclerView.Adapter<UserHolder> {

    private final List<User> users;
    private final int imageHeight;
    private final int imageWidth;

    private final OnItemClickListener onItemClickListener;

    private final View.OnClickListener internalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            User user = (User) view.getTag();
            onItemClickListener.onItemClick(view, user);
        }
    };

    public UsersAdapter(int imageHeight, int imageWidth, @NonNull OnItemClickListener onItemClickListener) {
        users = new ArrayList<>();
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.onItemClickListener = onItemClickListener;
    }

    public void changeDataSet(@NonNull List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return UserHolder.create(parent.getContext(), imageHeight, imageWidth);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);

        holder.itemView.setOnClickListener(internalListener);
        holder.itemView.setTag(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnItemClickListener {

        void onItemClick(@NonNull View view, @NonNull User user);

    }
}
