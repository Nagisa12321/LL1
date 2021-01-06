package com.jtchen.analyze;

import com.jtchen.structure.FirstSet;
import com.jtchen.structure.RightFirstSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 求串a的终结首符集First(a):
 * 1) 求出文法中每个符号的首符集
 * - 若x属于终结符, 则First(x) = {x};
 * - 若X为非终结符, 且有产生式X -> a...
 * 则将a加入到First(X)中; 若X -> ε,
 * 则ε也加入到First(X)中
 * - 若X -> Y1 Y2 ... Yk
 * <p>
 * {@code j = 0; First(X) = {}; // 初始化
 * <p>
 * REPEAT j = j + 1;
 * <p>
 * FIRST(X) = FIRST(X) 并 (FIRST(Yj) - {ε})
 * <p>
 * UNTIL ε 不属于 FIRST(Yj) || j = k
 * <p>
 * IF (j = k && ε 属于 FIRST(Yk))
 * <p>
 * THEN FIRST(X) = FIRST(X) 并 {ε}}
 *
 * @author jtchen
 * @version 1.0
 * @date 2021/1/5 12:22
 */
public class First {
    private static HashMap<String, List<List<String>>> map;

    public static List<FirstSet>
    getMapFirstSet(HashMap<String, List<List<String>>> m) {
        // 赋值给map
        map = m;

        // 待返回的first集合
        List<FirstSet> list = new ArrayList<>();

        // 分别找寻各元素非终结符
        for (var entry : map.entrySet()) {
            FirstSet first =
                    new FirstSet(entry.getKey(), new ArrayList<>(), new ArrayList<>());
            getFirst(first);
            list.add(first);
        }

        return list;
    }

    public static FirstSet getFirst(FirstSet X) {

        /* 1. 若x属于终结符, 则First(x) = {x}; */
        if (!map.containsKey(X.getKey())) {
            X.getSet().add(X.getKey());
            return X;
        }

        /* 2. 若X为非终结符, 且有产生式X -> a...
              则将a加入到First(X)中; 若X -> ε,
              则ε也加入到First(X)中。
           3. 若X -> Y1 Y2 ... Yk
              见类注释
        */
        var lists = map.get(X.getKey());// 右式

        for (var list : lists) {
            int j = 0, k = list.size();

            var tmpList = new ArrayList<String>();
            var FirstYj =
                    getFirst(new FirstSet(list.get(j), new ArrayList<>(), new ArrayList<>()));
            /*var FirstYj = getFirst(list.get(j));*/
            do {
                // FIRST(X) = FIRST(X) 并 (FIRST(Yj) - {ε})
                if (FirstYj.getSet().contains("ε")) {
                    List<String> tmp = new ArrayList<>(FirstYj.getSet());
                    tmp.remove("ε");
                    X.getSet().addAll(tmp);
                    tmpList.addAll(tmp);
                } else {
                    X.getSet().addAll(FirstYj.getSet());
                    tmpList.addAll(FirstYj.getSet());
                }

                // REPEAT j = j + 1;
                j++;
            } while ((FirstYj.getSet().contains("ε") && j != k));

            if (j == k) {
                var FirstYk =
                        getFirst(new FirstSet(list.get(k - 1), new ArrayList<>(), new ArrayList<>()));
                /* getFirst(list.get(k - 1)); */
                if (FirstYk.getSet().contains("ε")) {
                    X.getSet().add("ε");
                    tmpList.add("ε");
                }
            }
            X.getRightSets().add(new RightFirstSet(list, tmpList));
        }
        return X;
    }
}
