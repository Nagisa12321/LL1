package com.jtchen.input;

import static com.jtchen.analyze.LeftRecursion.*;
import static com.jtchen.analyze.Backtracking.*;
import static com.jtchen.tools.mapTool.*;

import java.util.ArrayList;
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
        var map = EliminateLeftRecursion(list);
        System.out.println("Eliminate left recursion: \n" + toMapString(map));

        map = EliminateBacktracking(map);

    }
}

