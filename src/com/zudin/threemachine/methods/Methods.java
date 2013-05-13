package com.zudin.threemachine.methods;

import com.zudin.threemachine.model.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Zudin Sergey, 272(2)
 * 16.04.13
 */
public class Methods {
    /**
     * Create order of details with minimum time to work
     *
     * @param list details
     * @return list of details in the right order
     */
    public static Way getSolution (ArrayList<Detail> list) {
        ArrayList<Way> ways = new ArrayList<Way>();
        for (Detail temp : list) {  //new way for each detail
            ways.add(new Way(temp));
        }
        if (ways.size() == 1) {
            return ways.get(0);
        }
        while (true) {
            /* For each way add new detail. Way must not has this detail in the queue */
            ArrayList<Way> newWays = new ArrayList<Way>();
            for (Way way : ways) {
                for (Detail detail : list) {
                    if (!way.contains(detail)) {
                        Way newWay = new Way(way);
                        newWay.addDetail(detail);
                        newWays.add(newWay);
                    }
                }
            }
            ways.clear();
            /* Create groups of ways with the same details in the queue */
            ArrayList<Group> groups = new ArrayList<Group>();
            while (!newWays.isEmpty()) {
                Group group = new Group();
                Way first = newWays.get(0);
                group.add(first);
                for (int i = 1; i < newWays.size(); i ++) {
                    if (newWays.get(i).isDetailsEquals(first)) {
                        group.add(newWays.get(i));
                        newWays.remove(i);
                        i --;
                    }
                }
                newWays.remove(0);
                groups.add(group);
            }
            /* Find best solution for each group and add it to the ways */
            for (Group group : groups) {
                ways.addAll(group.getBestWay());
            }
            /* If way has all details, this is the end of the cycle, continue otherwise */
            if (ways.get(0).getDetails().size() == list.size()) {
                break;
            }
        }
        Collections.sort(ways);
        return ways.get(0);
    }
}
