package com.bleyl.pictorial.models;

public interface Image {

    boolean hasDescription();

    String getDescription();

    String getLink();

    boolean isAnimated();

    boolean hasMP4Link();

    String getMP4Link();
}