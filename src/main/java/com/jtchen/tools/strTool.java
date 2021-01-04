package com.jtchen.tools;

import java.util.*;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/4 0:17
 */
public class strTool {
    /**
     * @param str 每一行输入的式子 e.g. S' -> a b c S' | @  --> S' -> {{a, b, c, S'}, { @}}
     *            (空格隔开)
     * @return 转换之后的Map映射
     */
    public static void ConversionMap(String str, HashMap<String, List<List<String>>> map) {
        List<List<String>> list = turnList(str);
        turnMap(list, map);
    }

    /**
     * 把字符串转换为list, 并检查格式有无错误
     *
     * @param str e.g. S' -> a b c S' | @  --> {{S'}, {a, b, c, S'}, { @}}
     * @return 把string转为List的格式组合
     */
    private static List<List<String>> turnList(String str) {
        String[] tmp = str.split(" ");

        // 第一个为‘|’, 第二个不为‘->’, 总个数小于3
        if (tmp.length < 3 || !tmp[1].equals("->") || tmp[0].equals("|"))
            throw new IllegalArgumentException("输入式子错误");

        // 有连续两个'|' 或者'-->' 的位置不对
        for (int i = 0; i < tmp.length - 1; i++) {
            if (tmp[i].equals("|") && tmp[i + 1].equals("|"))
                throw new IllegalArgumentException("输入式子错误");
            if (tmp[i].equals("->") && i != 1)
                throw new IllegalArgumentException("输入式子错误");
        }

        // 循环填入list中
        List<List<String>> list = new ArrayList<>();
        List<String> tmp1 = new ArrayList<>();
        for (String s : tmp) {
            if (s.equals("->") || s.equals("|")) {
                list.add(new ArrayList<>(tmp1));
                tmp1.clear();
            } else tmp1.add(s);
        }
        list.add(new ArrayList<>(tmp1));
        return list;
    }

    /**
     * 把格式组合转换为最终map形式
     *
     * @param list e.g. {{S'}, {a, b, c, S'}, { @}}
     *             -> S' -> {{a, b, c, S'}, { @}}
     */
    public static void turnMap
    (List<List<String>> list, Map<String, List<List<String>>> map) {
        String s = list.get(0).get(0);
        list.remove(0);

        // 如果map中存在s, 则在原有基础上添加
        if (map.containsKey(s)) map.get(s).addAll(list);
        else map.put(s, list);
    }

    /**
     * 给出一个式子, 提取其非终结符(左侧)
     * @param s e.g. "S -> Q c | c"
     * @return e.g. "S"
     */
    public static String FirstNonterminal(String s) {
        String[] strings = s.split(" ");
        return strings[0];
    }

    /**
     * 给出一个非终结符集合, 判断非终结符a是否排在S前面
     * @param strings 非终结符集合
     * @param a 非终结符a
     * @param S 非终结符S
     * @return a是否排在s前面
     */
    public static boolean isFront(String[] strings, String a, String S) {
        int idxA = 0;
        int idxS = 0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals(a)) idxA = i;
            if (strings[i].equals(S)) idxS = i;
        }

        return idxA < idxS;
    }
}