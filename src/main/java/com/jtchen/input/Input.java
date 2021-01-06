package com.jtchen.input;

import com.jtchen.analyze.Follow;
import com.jtchen.structure.FirstSet;
import com.jtchen.structure.FollowSet;
import com.jtchen.structure.Table;

import static com.jtchen.analyze.LeftRecursion.*;
import static com.jtchen.analyze.Backtracking.*;
import static com.jtchen.analyze.First.*;
import static com.jtchen.analyze.Follow.*;
import static com.jtchen.analyze.LL1.*;
import static com.jtchen.tools.mapTool.*;
import static com.jtchen.structure.FirstSet.*;
import static com.jtchen.structure.FollowSet.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/3 23:00
 */
public class Input {
    public static void main(String[] args) {
        System.out.println("请输入你想要的式子, 按#结束:");
        var list = new ArrayList<String>();
        Scanner c = new Scanner(System.in);
        while (true) {
            String str = c.nextLine();
            if (str.equals("#")) break;
            list.add(str);
        }
        // 消除左递归
        var map = EliminateLeftRecursion(list);
        System.out.println("Eliminate left recursion: \n" + toMapString(map));

        // 首符
        var first = getFirst();

        // 消除回溯
        map = EliminateBacktracking(map);

        // 非终结符
        var nonTerminalSet = NonTerminalSet(map);
        System.out.println("Non-terminal set: \n" + nonTerminalSet + '\n');

        // 终结符
        var terminatorSet = TerminatorSet(map);
        terminatorSet.add("#"); // 添加#
        terminatorSet.remove("ε");  // 去掉ε
        System.out.println("Terminal set: \n" + terminatorSet + '\n');

        // First集合
        List<FirstSet> firstLists = getMapFirstSet(map);
        System.out.println(toFirstString(firstLists));

        // Follow集合
        List<FollowSet> followList = getMapFollowSet(map, toFirstMap(firstLists), first);
        System.out.println(toFollowString(followList));

        // 构造分析表
        Table table = GenerateTable(
                map,
                nonTerminalSet,
                terminatorSet,
                toFirstMap(firstLists),
                toFollowMap(followList));
        table.printMap();
    }
}

