package com.jtchen.analyze;

import static com.jtchen.tools.strTool.*;
import static com.jtchen.tools.mapTool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消除直接左递归/ 间接左递归
 *
 * @author jtchen
 * @version 1.0
 * @date 2021/1/4 0:07
 */
public class LeftRecursion {
    /**
     * 传进来的表达式用哈希表来存储,哈希表左侧存储等式左边字母
     * 右侧是用|分割而成的List<String>组合
     *
     * @param list e.g. P -> Pa | B
     *             ...
     *             map: P -> {{P, a}, {B}}
     *             ...
     * @return 消除左递归后的式子们
     */
    public static HashMap<String, List<List<String>>>
    EliminateLeftRecursion(List<String> list) {
        var map = new HashMap<String, List<List<String>>>();
        for (String s : list) ConversionMap(s, map);

        // 首个非终结符
        String first = FirstNonterminal(list.get(0));

        System.out.println("Original:\n" + toMapString(map));
        // 消除左递归
        map = eliminate(map, first);

        return map;
    }

    /**
     * 消除直接左递归
     * P -> P a1 | P a2 | P a3 | ... | P an | b1 | b2 | b3 | ... | bn
     * <p>
     * P -> b1 P` | b2 P` | b3 P` | ... | bn P`
     * P` -> a1 P` | ... | an P`
     *
     * @param map 经过消除间接左递归后形成的式子
     */
    private static HashMap<String, List<List<String>>>
    eliminate(HashMap<String, List<List<String>>> map, String first) {
        /* 1. 为非终结符重新排序 */

        // 换为String数组
        String[] NonTerminal = map.keySet().toArray(String[]::new);

        // 把左递归式子非终结符放在最后
        for (int i = 0; i < NonTerminal.length; i++) {

            // 若为非终结符则放到最后 跳出循环
            if (NonTerminal[i].equals(first)) {
                NonTerminal[i] = NonTerminal[NonTerminal.length - 1];
                NonTerminal[NonTerminal.length - 1] = first;
                break;
            }
        }

        /* 2. 先代入式子, 若存在直接左递归, 则消除直接左递归 */

        for (int i = 0; i < NonTerminal.length; i++) {
            var list = map.get(NonTerminal[i]);
            for (int j = 0; j < i; j++) {
                String change = NonTerminal[j];

                for (int k = 0; k < list.size(); k++) {
                    for (int l = 0; l < list.get(k).size(); l++) {
                        String s = list.get(k).get(l);
                        if (s.equals(change)) {
                            var mapList = map.get(s);
                            int idx = list.get(k).indexOf(s);
                            list.get(k).remove(s);
                            var tmpRight = new ArrayList<>(list.get(k));
                            for (int m = 0; m < mapList.get(0).size(); m++) {
                                list.get(k).add(idx + m, mapList.get(0).get(m));
                            }
                            // 加入其他的
                            for (int m = 1; m < mapList.size(); m++) {
                                // mapList.get(m), tmpRight
                                var tmp = new ArrayList<>(tmpRight);

                                for (int n = 0; n < mapList.get(m).size(); n++) {
                                    tmp.add(idx + n, mapList.get(m).get(n));
                                }
                                list.add(tmp);
                            }
                        }
                    }
                }
            }
            /* 若存在直接左递归, 则消除直接左递归 */
            EliminationFormula(map, NonTerminal[i]);
        }


        /* 3. 消除无用非终结符 */
        var list = new ArrayList<String>();
        dfs(map, list, first);
        var tmp = new HashMap<String, List<List<String>>>();
        for (String str : list) {
            tmp.put(str, map.get(str));
        }
        return tmp;
    }

    /**
     * 为单个式子消除左递归
     *
     * @param map   消除左递归的map
     * @param first 式子开头的非终结符
     */
    private static void
    EliminationFormula(HashMap<String, List<List<String>>> map, String first) {

        // 左递归式子
        List<List<String>> contains = new ArrayList<>();

        // 非左递归式子
        List<List<String>> notContains = new ArrayList<>();

        var list1 = new ArrayList<String>();
        list1.add(first + '`');
        contains.add(new ArrayList<>(list1));

        for (var list2 : map.get(first)) {
            if (list2.get(0).equals(first)) contains.add(list2);
            else notContains.add(list2);
        }

        // 如果无左递归式子, 则下一个
        if (contains.size() == 1) return;

        // 处理左递归右式子
        for (int j = 0; j < contains.size(); j++) {
            if (j != 0) {
                contains.get(j).remove(0);
                contains.get(j).add(first + '`');
            }
        }

        // 处理非左递归右边式子
        for (var list2 : notContains) {
            list2.add(first + '`');
        }

        map.put(first, notContains);

        list1.clear();
        list1.add("ε");
        contains.add(list1);

        turnMap(contains, map);
    }
}
