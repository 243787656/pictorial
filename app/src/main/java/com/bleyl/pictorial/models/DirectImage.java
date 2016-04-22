package com.bleyl.pictorial.models;

public class DirectImage implements Image {

    private String link;

    public DirectImage(String link) {
        this.link = link;
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
        return link;
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
