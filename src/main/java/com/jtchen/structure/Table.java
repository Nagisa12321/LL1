package com.jtchen.structure;

import static com.jtchen.tools.strTool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/6 2:07
 */
public class Table {
    private final HashMap<String, HashMap<String, List<String>>> map;

    private final Set<String> nonTerminalSet;
    private final Set<String> terminatorSet;

    public Set<String> getNonTerminalSet() {
        return nonTerminalSet;
    }

    public Set<String> getTerminatorSet() {
        return terminatorSet;
    }

    public Table(Set<String> nonTerminalSet, Set<String> terminatorSet) {
        this.nonTerminalSet = nonTerminalSet;
        this.terminatorSet = terminatorSet;
        map = new HashMap<>();
        // 生成表
        for (var l1 : nonTerminalSet) {
            var tmp = new HashMap<String, List<String>>();
            for (var l2 : terminatorSet) {
                tmp.put(l2, new ArrayList<>());
            }
            map.put(l1, tmp);
        }
    }

    public List<String> getList(String nonTerminalSet, String terminatorSet) {
        return map.get(nonTerminalSet).get(terminatorSet);
    }

    public void printMap() {
        System.out.println("map:");
        System.out.printf("%-20s", "");
        for (var x : terminatorSet) {
            System.out.printf("%-20s", x);
        }
        System.out.println();
        for (var y : nonTerminalSet) {
            System.out.printf("%-20s", y);
            for (var x : terminatorSet) {
                var list = getList(y, x);
                if (list.size() == 0)
                    System.out.printf("%-20s", "null");
                else System.out.printf("%-20s", toProduction(y, list));
            }
            System.out.println();
        }
        System.out.println();
    }
}
