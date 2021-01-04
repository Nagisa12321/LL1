package com.jtchen.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 中间产物, 用于提取左因子遍历, 后续也可使用
 *
 * @author jtchen
 * @version 1.0
 * @date 2021/1/4 23:35
 */
public class MidProduct {

    // key
    private final String key;

    public String getKey() {
        return key;
    }

    // 对应右式
    private List<List<String>> list;

    public void setList(List<List<String>> list) {
        this.list = list;
    }

    public List<List<String>> getList() {
        return list;
    }

    public MidProduct(String key, List<List<String>> list) {
        this.key = key;
        this.list = list;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(key)
                .append(" -> ");
        for (int i = 0; i < list.size(); i++) {
            for (String s : list.get(i))
                builder.append(s).append(" ");
            if (i != list.size() - 1) builder.append("| ");
        }
        return builder.toString();
    }

    /* hashmap转化为Mid */
    public static List<MidProduct> toMid(HashMap<String, List<List<String>>> map) {
        var list = new ArrayList<MidProduct>();
        for (var entry : map.entrySet())
            list.add(new MidProduct(entry.getKey(), entry.getValue()));
        return list;
    }

    /* Mid转化为HashMap */
    public static HashMap<String, List<List<String>>> toMap(List<MidProduct> products) {
        var map = new HashMap<String, List<List<String>>>();
        for (var product : products)
            map.put(product.key, product.list);
        return map;
    }
}
