package com.bleyl.pictorial.models;

public class DirectImage implements Image {

    private String mLink;

    public DirectImage(String link) {
        mLink = link;
    }

    @Override
    public boolean hasDescription() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getLink() {
        return mLink;
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public boolean hasMP4Link() {
        return false;
    }

    @Override
    public String getMP4Link() {
        return null;
    }
}
