package com.jtchen.structure;

import java.util.HashMap;
import java.util.List;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/5 13:14
 */
public class FirstSet {

    private final String key;

    public String getKey() {
        return key;
    }

    private final List<String> set;

    public List<String> getSet() {
        return set;
    }


    private final List<RightFirstSet> rightSets; // 对应右式

    public List<RightFirstSet> getRightSets() {
        return rightSets;
    }

    public FirstSet(String key, List<String> set, List<RightFirstSet> rightSets) {
        this.key = key;
        this.set = set;
        this.rightSets = rightSets;
    }

    /**
     * 通过list获得right first set
     *
     * @param list list
     * @return right first set
     */
    public List<String> getRightFirstSet(List<String> list) {
        for (var r : rightSets) {
            if (list.equals(r.getKey())) return r.getSet();
        }
        return null;
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

    public static String toFirstString(List<FirstSet> sets) {
        StringBuilder builder = new StringBuilder();
        builder.append("First set: \n");
        for (FirstSet set : sets) builder.append(set).append('\n');

        builder.append("\nFirst mapping: \n");
        for (FirstSet set : sets) {
            builder.append(set.getKey()).append(" -> \n");
            for (var list : set.getRightSets())
                builder.append(list).append('\n');
        }
        return builder.toString();
    }

    public static HashMap<String, FirstSet> toFirstMap(List<FirstSet> sets) {
        var map = new HashMap<String, FirstSet>();
        for (var set : sets)
            map.put(set.key, set);
        return map;
    }
}
