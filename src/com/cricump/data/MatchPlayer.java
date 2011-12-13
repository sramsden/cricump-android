package com.cricump.data;

public class MatchPlayer {

    private int id;
    private String descriptor;

    public MatchPlayer(int id, String descriptor) {
        this.id = id;
        this.descriptor = descriptor;
    }

    public int getId() {
        return id;
    }

    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public String toString() {
        return descriptor;
    }
}
