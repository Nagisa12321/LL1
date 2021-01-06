package com.jtchen.analyze;

import com.jtchen.structure.FirstSet;
import com.jtchen.structure.FollowSet;
import static com.jtchen.tools.strTool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/1/6 0:00
 */
public class Follow {
    private static HashMap<String, List<List<String>>> map;
    private static String first;
    private static HashMap<String, FirstSet> firstMap;

    public static List<FollowSet>
    getMapFollowSet(HashMap<String, List<List<String>>> m, HashMap<String, FirstSet> fm, String f) {
        // 赋值给map, first, firstMap
        map = m;
        first = f;
        firstMap = fm;

        // 待返回的follow集合
        List<FollowSet> list = new ArrayList<>();

        for (var entry : map.entrySet()) {
            var follow = new FollowSet(entry.getKey(), new ArrayList<>());
            follow = getFollow(follow, new HashMap<>());
            list.add(follow);
        }

        return list;
    }

    private static FollowSet getFollow(FollowSet followSet, HashMap<String, FollowSet> tmpMap) {
        // 已经求过的话不必再求一遍
        if (tmpMap.containsKey(followSet.getKey()))
            return tmpMap.get(followSet.getKey());
        else tmpMap.put(followSet.getKey(), followSet);

        /* 1. 如果是开始符号, 则添加# */
        if (followSet.getKey().equals(first))
            followSet.getSet().add("#");

        /* 2. 若为 B -> aAb, First(b) - ε加入follow(A) */
        /* 3. 若为 B -> aA || B -> aAb (b -+-> ε) Follow(A) += Follow(B) */

        // 找寻有没有含有follow.getKey() 的项目
        for (var entry : map.entrySet()) {
            for (var lists : entry.getValue()) {
                for (int i = 0; i < lists.size(); i++) {
                    String s = lists.get(i);
                    if (s.equals(followSet.getKey())) {
                        // 若为 B -> aAb, First(b) - ε加入follow(A)
                        // 后边还有元素, 则符合B -> aAb形式
                        if (i < lists.size() - 1) {
                            String tmpStr = lists.get(i + 1);
                            List<String> list;
                            if (firstMap.containsKey(tmpStr))
                            list = new ArrayList<>(firstMap.get(lists.get(i + 1)).getSet());
                            else {
                                list = new ArrayList<>();
                                list.add(tmpStr);
                            }

                            // 若为 B -> aAb (b -+-> ε) Follow(A) += Follow(B)
                            if (list.contains("ε")) {
                                var tmp =
                                        getFollow(new FollowSet(entry.getKey(), new ArrayList<>()), tmpMap);
                                followSet.getSet().addAll(tmp.getSet());
                                list.remove("ε");
                            }
                            followSet.getSet().addAll(list);

                        }

                        // 若为 B -> aA  Follow(A) += Follow(B)
                        else if (i == lists.size() - 1) {
                            // 如果是本身, 啥也不做, 否则
                            if (!entry.getKey().equals(s)) {
                                var tmp =
                                        getFollow(new FollowSet(entry.getKey(), new ArrayList<>()), tmpMap);
                                followSet.getSet().addAll(tmp.getSet());
                            }
                        }
                    }
                }
            }
        }
        removeDuplicate(followSet.getSet());
        return followSet;
    }
}
