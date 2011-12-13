package com.cricump.data;

public class Match {

    private long id;
    private String descriptor;
    private String url;
    private String scheduledStart;
    private String scheduledStartString; // TODO: currently not using .. need it?
    private String commentaryUrl;
    private String rankingUrl;
    private String matchesUrl;

    private HistoryItem[] recentHistoryItems;
    private HistoryItem[] moreHistoryItems;
    private Ranking[] rankings = new Ranking[]{};

    public HistoryItem[] getMoreHistoryItems() {
        return moreHistoryItems;
    }

    public void setMoreHistoryItems(HistoryItem[] moreHistoryItems) {
        this.moreHistoryItems = moreHistoryItems;
    }

    public HistoryItem[] getRecentHistoryItems() {
        return recentHistoryItems;
    }

    public HistoryItem[] addRecentHistoryItem(HistoryItem historyItem){
        HistoryItem[] his = {historyItem};
        return HistoryItem.concat(recentHistoryItems,his);
    }

    public HistoryItem[] addRecentHistoryItems(HistoryItem[] historyItems){
        recentHistoryItems = HistoryItem.concat(recentHistoryItems,historyItems);
        return recentHistoryItems;
    }

    public void setRecentHistoryItems(HistoryItem[] recentHistoryItems) {
        this.recentHistoryItems = recentHistoryItems;
    }

    public Match(long id, String descriptor, String url) {
        this.id = id;
        this.descriptor = descriptor;
        this.url = url;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(String scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public String getScheduledStartString() {
        return scheduledStartString;
    }

    public void setScheduledStartString(String scheduledStartString) {
        this.scheduledStartString = scheduledStartString;
    }

    public String getCommentaryUrl() {
        return commentaryUrl;
    }

    public void setCommentaryUrl(String commentaryUrl) {
        this.commentaryUrl = commentaryUrl;
    }

    public String getRankingUrl() {
        return rankingUrl;
    }

    public void setRankingUrl(String rankingUrl) {
        this.rankingUrl = rankingUrl;
    }

    public String getMatchesUrl() {
        return matchesUrl;
    }

    public void setMatchesUrl(String matchesUrl) {
        this.matchesUrl = matchesUrl;
    }

    public Ranking[] getRankings() {
        return rankings;
    }

    public void setRankings(Ranking[] rankings) {
        this.rankings = rankings;
    }

    public long getId(){
        return id;
    }

}
