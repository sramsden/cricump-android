package com.cricump.data;

public class HistoryItem {
    private String id; // eg, "11326715278"
    private String createdAt; // eg, "10:03.48 "
    private String colour;  // eg, "#33ffff"
    private String content; // eg, "Runs 6 Tally 6 Score 3/162"

    public String toString(){
        return createdAt + " <font color='" + colour + "'>" + content + "</font>";
    }

    public boolean equals(HistoryItem other){
        return (other != null && this.toString().equals(other.toString()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static HistoryItem[] concat(HistoryItem[] a, HistoryItem[] b) {
        if(a == null) return b;
        if(b == null) return a;
        HistoryItem[] c = new HistoryItem[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
