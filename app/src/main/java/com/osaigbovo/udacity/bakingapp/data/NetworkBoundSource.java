package com.osaigbovo.udacity.bakingapp.data;

import android.annotation.SuppressLint;
import android.util.Log;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public abstract class NetworkBoundSource<LocalType, RemoteType> {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    protected NetworkBoundSource(FlowableEmitter<Resource<LocalType>> emitter) {
        Disposable firstDataDisposable = getLocal()
                .map(Resource::loading)
                .subscribe(emitter::onNext);

        getRemote().map(mapper())
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(localTypeData -> {
                    firstDataDisposable.dispose();
                    NetworkBoundSource.this.saveCallResult(localTypeData);
                    NetworkBoundSource.this.saveCallPref(localTypeData);
                    NetworkBoundSource.this.getLocal().map(Resource::success).subscribe(emitter::onNext);
                }, throwable -> Log.e("NetworkBoundSource", throwable.getMessage()));
    }

    public abstract Single<RemoteType> getRemote();

    public abstract Flowable<LocalType> getLocal();

    public abstract void saveCallResult(LocalType data);

    public abstract void saveCallPref(LocalType data);

    public abstract Function<RemoteType, LocalType> mapper();

}