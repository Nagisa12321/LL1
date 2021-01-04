package com.jtchen.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static void dfs(Map<String, List<List<String>>> map, List<String> list, String start) {

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
}
