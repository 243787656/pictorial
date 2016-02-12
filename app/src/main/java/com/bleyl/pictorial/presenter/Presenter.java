package com.bleyl.pictorial.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}