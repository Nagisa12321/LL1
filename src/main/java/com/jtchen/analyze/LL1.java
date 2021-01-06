package com.jtchen.analyze;

import com.jtchen.structure.FirstSet;
import com.jtchen.structure.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/6 1:35
 */
public class LL1 {
    // 生成分析表
    public static Table
    GenerateTable(HashMap<String, List<List<String>>> map,
                  Set<String> nonTerminalSet,
                  Set<String> terminatorSet,
                  HashMap<String, FirstSet> FirstMap,
                  HashMap<String, List<String>> FollowMap) {
        Table table = new Table(nonTerminalSet, terminatorSet);

        for (var entry : map.entrySet()) {
            for (var lists : entry.getValue()) {
                for (var tS : terminatorSet) {

                    /* 如果终结符tS属于First(entry.getKey()) */
                    if (FirstMap.get(entry.getKey()).getSet().contains(tS)) {
                        FirstSet firstSet = FirstMap.get(entry.getKey());
                        var rightFirst = firstSet.getRightFirstSet(lists);

                        if (rightFirst.contains(tS)) {
                            var list = table.getList(entry.getKey(), tS);
                            list.addAll(lists);
                        }
                    }
                }
            }

            /* 如果ε属于First(A), 则对任何属于Follow(A) 的终结符将A -> ε加入到 M[A, b]中 */
            if (FirstMap.get(entry.getKey()).getSet().contains("ε")) {
                var lists = FollowMap.get(entry.getKey());
                for (var s : lists) {
                    var list = table.getList(entry.getKey(), s);
                    list.add("ε");
                }
            }
        }
        return table;
    }
}
