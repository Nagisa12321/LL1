package com.jtchen.analyze;

import com.jtchen.structure.StackAndResult;
import com.jtchen.structure.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/6 10:14
 */
public class Parser {
    private Stack<String> stack;
    private final Table table;
    private final String first;

    public Parser(Table table, String first) {
        this.table = table;
        this.first = first;
    }

    /**
     * 根据当前读头==读头下面的元素和栈顶改变栈
     *
     * @param s 当前读头下面的符号
     * @return 是否可以接着读
     */
    private boolean StateChange(String s) throws IllegalArgumentException {
        // 如果读头是未知终结符, 报错
        if (!table.getTerminatorSet().contains(s))
            throw new IllegalArgumentException(s +
                    " do not exist in the Non-terminal set");
        else {
            String peek = stack.peek();
            // 如果栈顶为终结符, 则推移idx, 出栈
            if (table.getTerminatorSet().contains(peek)
                    && peek.equals(s)) {
                stack.pop();
                return true;
            }

            // 如果栈顶为非终结符, 匹配table, 并且出栈, 入栈匹配内容
            else if (table.getNonTerminalSet().contains(peek)) {
                stack.pop();
                var list = table.getList(peek, s);

                if (list.size() == 0)
                    throw new IllegalArgumentException("The content in the match table is empty");

                // 反向入栈
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (!list.get(i).equals("ε"))
                        stack.push(list.get(i));
                }

            } else throw new IllegalArgumentException("error");
            return false;
        }
    }

    public StackAndResult isSentence(String str) {
        //新建栈并放入'#'、 first
        stack = new Stack<>();
        stack.push("#");
        stack.push(first);
        //当前下标
        int index = 0;
        String[] tmp = str.split(" ");
        List<String> list = new ArrayList<>(Arrays.asList(tmp));

        if (list.size() == 1 && list.get(0).equals("")) list.clear();

        list.add("#");
        StringBuilder builder = new StringBuilder();
        while (!stack.isEmpty()) {
            builder.append(stack).append('\n');
            try {
                if (StateChange(list.get(index))) index++;
            } catch (IllegalArgumentException e) {
                return new StackAndResult(builder.toString(), false);
            }
        }
        return new StackAndResult(builder.toString(), true);
    }
}
