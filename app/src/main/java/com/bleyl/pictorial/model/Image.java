package com.bleyl.pictorial.model;

public interface Image {

    boolean hasDescription();

    String getDescription();

    String getLink();

    boolean isAnimated();

    boolean hasMP4Link();

    String getMP4Link();
}