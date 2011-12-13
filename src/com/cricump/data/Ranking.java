package com.cricump.data;

public class Ranking {
    private String user;// user: "nic"
    private int total; // total: "1471"
    private String latestPrediction; //  latest_prediction: "8"

    public Ranking(String user, int total, String latestPrediction) {
        this.user = user;
        this.total = total;
        this.latestPrediction = latestPrediction;
    }

    public String getUser() {
        return user;
    }

    public int getTotal() {
        return total;
    }

    public String getLatestPrediction() {
        return latestPrediction;
    }

    public String toString(){
        return String.format("%04d", total) + " " + user + " - " + latestPrediction + " Runs";
    }

}
