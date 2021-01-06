package com.jtchen.structure;

import java.util.List;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/5 21:48
 */
public class RightFirstSet {
    private final List<String> key;

    public List<String> getKey() {
        return key;
    }

    private final List<String> set;

    public List<String> getSet() {
        return set;
    }

    public RightFirstSet(List<String> key, List<String> set) {
        this.key = key;
        this.set = set;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("First(");

        for (int i = 0; i < key.size(); i++) {
            builder.append(key.get(i));
            if (i != key.size() - 1)
                builder.append(" ");
        }

        builder.append(") = {");
        for (int i = 0; i < set.size(); i++) {
            if (i == set.size() - 1)
                builder.append(set.get(i));
            else builder.append(set.get(i)).append(", ");
        }
        builder.append("}");
        return builder.toString();
    }
}
