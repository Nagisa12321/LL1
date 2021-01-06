package com.jtchen.tools;

import java.util.*;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/4 1:12
 */
public class mapTool {

    /* map可视化 */
    public static String toMapString(HashMap<String, List<List<String>>> map) {
        StringBuilder builder = new StringBuilder();
        for (var entry : map.entrySet()) {
            builder.append(entry.getKey())
                    .append(" -> ");
            var list = entry.getValue();
            for (int i = 0; i < list.size(); i++) {
                for (String s : list.get(i))
                    builder.append(s).append(" ");
                if (i != list.size() - 1) builder.append("| ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * 深度遍历查询所有用过的非终结符
     * @param map 消除左递归后总表
     * @param list 待添加的非终结符表
     * @param start 开始字符
     */
    public static void dfs
    (Map<String, List<List<String>>> map, List<String> list, String start) {

        // 不是非终结符
        if (!map.containsKey(start)) return;

        // 已经加入
        if (list.contains(start)) return;

        list.add(start);
        var elements = map.get(start);

        for (var list1 : elements) {
            for (var s : list1) {
                dfs(map, list, s);
            }
        }
    }

    /**
     * 同过map求出非终结符集
     * @param map 诸多式子
     * @return 非终结符集合
     */
    public static Set<String> NonTerminalSet(HashMap<String, List<List<String>>> map){
        return map.keySet();
    }

    /**
     * 同过map求出终结符集
     * @param map 诸多式子
     * @return 终结符集合
     */
    public static Set<String> TerminatorSet(HashMap<String, List<List<String>>> map) {
        var set = new HashSet<String>();
        for (var entry : map.entrySet()) {
            for (var lists : entry.getValue()) {
                for (var s : lists) {
                    if (!map.containsKey(s)) set.add(s);
                }
            }
        }
        return set;
    }
}
