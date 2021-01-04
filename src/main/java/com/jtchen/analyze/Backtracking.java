package com.jtchen.analyze;

import com.jtchen.structure.MidProduct;

import static com.jtchen.structure.MidProduct.*;
import static com.jtchen.tools.mapTool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/4 22:11
 */
public class Backtracking {
    /**
     * 对传进来的式子消除回溯
     *
     * @param map 待消除回溯哈希表
     * @return 已经消除回溯哈希表
     */
    public static HashMap<String, List<List<String>>>
    EliminateBacktracking(HashMap<String, List<List<String>>> map) {
        // 消除左因子
        map = extractLeftFactor(map);
        System.out.println("Eliminate the left factor: \n" + toMapString(map));

        return map;
    }

    /**
     * 提取左因子
     * e.g.
     * A -> a A B | a | b
     * <p>
     * turn to
     * A -> a A` | b
     * A` -> A B | ε
     *
     * @param map e.g. A -> a b1 | a b2 | ... | a bi | bi+1 | ... | bj
     * @return A -> a A` | bi+1 | ... | bj
     * A` -> b1 | b2 | b3 ... | bi
     */
    private static HashMap<String, List<List<String>>>
    extractLeftFactor(HashMap<String, List<List<String>>> map) {
        // 转化为中间产物方便操作遍历
        var products = toMid(map);

        // 遍历并且逐个提左因子
        for (int i = 0; i < products.size(); i++) {
            var lists = products.get(i).getList();

            // 左因子
            String left = null;

            // 遍历查看是否有左因子
            p:
            for (int j = 0; j < lists.size(); j++) {
                var tmp = lists.get(j).get(0);

                for (int k = j + 1; k < lists.size(); k++) {
                    if (tmp.equals(lists.get(k).get(0))){
                        left = lists.get(k).get(0);
                        break p;
                    }
                }
            }

            // 若无左因子则进入下一轮循环
            if (left == null) continue;

            // A -> aA` | b
            var right1 = new ArrayList<List<String>>();
            // A` -> AB | ε
            var right2 = new ArrayList<List<String>>();

            var tmp = new ArrayList<String>();
            tmp.add(left);
            tmp.add(products.get(i).getKey() + '"');
            right1.add(new ArrayList<>(tmp));

            for (List<String> list : lists) {

                // 如果是有左因子的式子, 则在right2添加list削去左因子之后的分式子
                // 如果没有左因子, 则在right1原封不同添加
                if (list.get(0).equals(left)) {
                    list.remove(0);
                    if (list.size() == 0) {
                        tmp.clear();
                        tmp.add("ε");
                        right2.add(new ArrayList<>(tmp));
                    } else {
                        right2.add(new ArrayList<>(list));
                    }
                } else
                    right1.add(new ArrayList<>(list));

            }

            // 更新products
            products.get(i).setList(right1);
            products.add(new MidProduct(products.get(i).getKey() + '"', right2));
        }

        return toMap(products);
    }
}

