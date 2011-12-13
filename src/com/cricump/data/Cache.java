package com.cricump.data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map cache = new HashMap();

    public static void setMatchDescriptors(String[] matchDescriptors) {
        cache.put(matchDescriptorsCacheKey(), matchDescriptors);
    }

    public static String[] getMatchDescriptors() {
        return (String[]) cache.get(matchDescriptorsCacheKey());
    }

    private static String matchDescriptorsCacheKey() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        return "match-descriptors-" + day;
    }

    public static void setMatch(String descriptor, Match match) {
        cache.put(descriptor, match);
    }

    public static Match getMatch(String descriptor) {
        return (Match) cache.get(descriptor);
    }

}
