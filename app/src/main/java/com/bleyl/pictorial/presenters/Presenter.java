package com.bleyl.pictorial.presenters;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}