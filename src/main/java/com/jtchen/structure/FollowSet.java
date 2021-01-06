package com.jtchen.structure;

import java.util.HashMap;
import java.util.List;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/6 0:01
 */
public class FollowSet {
    private final String key;

    public String getKey() {
        return key;
    }

    private final List<String> set;

    public List<String> getSet() {
        return set;
    }

    public FollowSet(String key, List<String> set) {
        this.key = key;
        this.set = set;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("First(").append(key).append(") = {");
        for (int i = 0; i < set.size(); i++) {
            if (i == set.size() - 1)
                builder.append(set.get(i));
            else builder.append(set.get(i)).append(", ");
        }
        builder.append("}");
        return builder.toString();
    }

    public static String toFollowString(List<FollowSet> sets) {
        StringBuilder builder = new StringBuilder();
        builder.append("Follow set: \n");
        for (FollowSet set : sets) builder.append(set).append('\n');
        return builder.toString();
    }

    public static HashMap<String, List<String>> toFollowMap(List<FollowSet> sets) {
        var map = new HashMap<String, List<String>>();
        for (var set : sets)
            map.put(set.key, set.set);
        return map;
    }
}
