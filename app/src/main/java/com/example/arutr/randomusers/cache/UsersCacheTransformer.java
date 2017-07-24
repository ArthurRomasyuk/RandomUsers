package com.example.arutr.randomusers.cache;


import com.example.arutr.randomusers.model.User;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Artur Romasiuk
 */

public class UsersCacheTransformer implements Observable.Transformer<List<User>, List<User>> {

    private final Func1<List<User>, Observable<List<User>>> saveFunc = users -> {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.delete(User.class);
            realm.insert(users);
        });
        return Observable.just(users);
    };

    private final Func1<Throwable, Observable<List<User>>> cacheErrorHandler = throwable -> {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> results = realm.where(User.class).findAll();
        return Observable.just(realm.copyFromRealm(results));
    };

    @Override
    public Observable<List<User>> call(Observable<List<User>> usersObservable) {
        return usersObservable
                .flatMap(saveFunc)
                .onErrorResumeNext(cacheErrorHandler);
    }
}
