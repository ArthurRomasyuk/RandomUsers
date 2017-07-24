package com.example.arutr.randomusers.screen.users;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import com.example.arutr.randomusers.R;
import com.example.arutr.randomusers.model.User;
import com.example.arutr.randomusers.model.UserResponse;
import com.example.arutr.randomusers.network.ApiFactory;
import com.example.arutr.randomusers.screen.details.UserDetailsActivity;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Artur Romasiuk
 */
public class UsersActivity extends AppCompatActivity implements UsersAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView usersRecycler;

    @BindView(R.id.empty)
    View emptyView;

    private UsersAdapter adapter;
    private String URL = "https://randomuser.me/";

    @Nullable
    private Subscription usersSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        int columns = getResources().getInteger(R.integer.columns_count);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), columns);
        usersRecycler.setLayoutManager(layoutManager);
        adapter = createAdapter();
        usersRecycler.setAdapter(adapter);
        String intentUrl = getIntent().getStringExtra("url");
        try {
            if(!intentUrl.isEmpty()){
                URL = intentUrl;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        usersSubscription = getSubscribe(URL);
    }

    public Subscription getSubscribe(String baseURL) {
        return ApiFactory.getUsersService(baseURL)
                .allUsers()
                .map(UserResponse::getUsers)
                .flatMap(users -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(User.class);
                        realm.insert(users);
                    });
                    return Observable.just(users);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<User> results = realm.where(User.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showUsers, this::showError);
    }

    @Override
    protected void onPause() {
        if (usersSubscription != null) {
            usersSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    public void onItemClick(@NonNull View view, @NonNull User user) {
        ImageView imageView = ButterKnife.findById(view, R.id.image);
        UserDetailsActivity.navigate(this, imageView, user);
    }

    private void showError(Throwable throwable) {
        usersRecycler.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        Log.d(getClass().getName(), "onError: " + throwable);

    }

    private void showUsers(@NonNull List<User> users) {
        adapter.changeDataSet(users);
        usersRecycler.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @NonNull
    private UsersAdapter createAdapter() {
        TypedValue typedValue = new TypedValue();
        getResources().getValue(R.dimen.rows_count, typedValue, true);
        float rowsCount = typedValue.getFloat();
        int actionBarHeight = getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)
                ? TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics())
                : 0;
        int imageHeight = (int) ((getResources().getDisplayMetrics().heightPixels - actionBarHeight) / rowsCount);

        int columns = getResources().getInteger(R.integer.columns_count);
        int imageWidth = getResources().getDisplayMetrics().widthPixels / columns;

        return new UsersAdapter(imageHeight, imageWidth, this);
    }
}
