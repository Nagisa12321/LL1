package com.jtchen.input;

import com.jtchen.analyze.Parser;
import com.jtchen.structure.FirstSet;
import com.jtchen.structure.FollowSet;
import com.jtchen.structure.StackAndResult;
import com.jtchen.structure.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jtchen.analyze.Backtracking.EliminateBacktracking;
import static com.jtchen.analyze.First.getMapFirstSet;
import static com.jtchen.analyze.Follow.getMapFollowSet;
import static com.jtchen.analyze.LL1.GenerateTable;
import static com.jtchen.analyze.LeftRecursion.EliminateLeftRecursion;
import static com.jtchen.analyze.LeftRecursion.getFirst;
import static com.jtchen.structure.FirstSet.toFirstMap;
import static com.jtchen.structure.FirstSet.toFirstString;
import static com.jtchen.structure.FollowSet.toFollowMap;
import static com.jtchen.structure.FollowSet.toFollowString;
import static com.jtchen.tools.mapTool.*;
import static com.jtchen.tools.strTool.toProduction;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/6 11:01
 */
public class inputGUI {
    private Parser parser;
    private JPanel LL1;
    private JTextArea text_grammar;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JTextArea textArea5;
    private JTextArea textArea6;
    private JButton createTableButton;
    private JButton analysButton;
    private JTextArea textArea7;
    private JButton resetButton;
    private JTable table1;
    private JTextArea textArea8;
    private JButton defaultButton;
    private JTextField textField1;
    private DefaultTableModel tableModel;

    public inputGUI() {
        text_grammar.setLineWrap(true);
        textArea1.setLineWrap(true);
        textArea2.setLineWrap(true);
        textArea3.setLineWrap(true);
        textArea4.setLineWrap(true);
        textArea5.setLineWrap(true);
        textArea6.setLineWrap(true);
        textArea7.setLineWrap(true);
        textArea8.setLineWrap(true);
        analysButton.setEnabled(false);

        createTableButton.addActionListener(e -> {
            String s = text_grammar.getText();
            String[] tmp = s.split("\n");
            List<String> list = new ArrayList<>(Arrays.asList(tmp));

            try {
                // 消除左递归
                var map = EliminateLeftRecursion(list);
                textArea5.setText(toMapString(map));

                // 首符
                var first = getFirst();

                // 消除回溯
                map = EliminateBacktracking(map);
                textArea6.setText(toMapString(map));

                // 非终结符
                var nonTerminalSet = NonTerminalSet(map);
                textArea1.setText(nonTerminalSet.toString());

                // 终结符
                var terminatorSet = TerminatorSet(map);
                terminatorSet.add("#"); // 添加#
                terminatorSet.remove("ε");  // 去掉ε
                textArea2.setText(terminatorSet.toString());

                // First集合
                List<FirstSet> firstLists = getMapFirstSet(map);
                textArea3.setText(toFirstString(firstLists));

                // Follow集合
                List<FollowSet> followList = getMapFollowSet(map, toFirstMap(firstLists), first);
                textArea4.setText(toFollowString(followList));

                // 构造分析表
                Table table = GenerateTable(
                        map,
                        nonTerminalSet,
                        terminatorSet,
                        toFirstMap(firstLists),
                        toFollowMap(followList));
                table.printMap();

                tableModel = (DefaultTableModel) table1.getModel();
                tableModel.addColumn("");
                for (var a : table.getTerminatorSet())
                    tableModel.addColumn(a);
                for (var y : nonTerminalSet) {
                    Object[] objects = new Object[table.getTerminatorSet().size() + 1];
                    int idx = 0;
                    objects[idx++] = y;
                    for (var x : terminatorSet) {
                        var list1 = table.getList(y, x);
                        if (list1.size() == 0) objects[idx++] = "null";
                        else objects[idx++] = toProduction(y, list1);
                        /*tableModel.addColumn(toProduction(y, list1));*/
                    }
                    tableModel.addRow(objects);
                }

                var theFirst = getFirst();
                // 分析字符串
                parser = new Parser(table, theFirst);
                analysButton.setEnabled(true);
                createTableButton.setEnabled(false);
                defaultButton.setEnabled(false);
            } catch (IllegalArgumentException m) {
                JOptionPane.showMessageDialog(null, m.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        });
        defaultButton.addActionListener(e -> text_grammar.setText("E -> E + T | T\n" +
                "T -> T * F | F\n" +
                "F -> ( E ) | i"));
        analysButton.addActionListener(e -> {
            StackAndResult tmp = parser.isSentence(textArea7.getText());
            textArea8.setText(tmp.getStackLine());
            if (tmp.isSuccess())
                textField1.setText("GOOD!");
            else textField1.setText("Failed- -");
        });
        resetButton.addActionListener(e -> {
            analysButton.setEnabled(false);
            createTableButton.setEnabled(true);
            defaultButton.setEnabled(true);

            text_grammar.setText("");
            textArea1.setText("");
            textArea2.setText("");
            textArea3.setText("");
            textArea4.setText("");
            textArea5.setText("");
            textArea6.setText("");
            textArea7.setText("");
            textArea8.setText("");
            textField1.setText("");
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("inputGUI");
        frame.setContentPane(new inputGUI().LL1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(250, 250);
        frame.pack();
        frame.setVisible(true);
    }
}
