package com.zudin.threemachine.model;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 16.04.13
 */
public class Group {
    ArrayList<Way> ways = new ArrayList<Way>();

    public void add (Way way) {
        if (ways.isEmpty() || ways.get(0).isDetailsEquals(way)) {
            ways.add(way);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<Way> getBestWay () {
        ArrayList<Way> result = new ArrayList<Way>();
        result.add(ways.get(0));
        for (int j = 1; j < ways.size(); j ++ ) {
            Way way = ways.get(j);
            for (int i = 0; i < result.size(); i ++) {
                Way best = result.get(i);
                int c = best.getTimes()[2] - way.getTimes()[2];
                int b = best.getTimes()[1] - way.getTimes()[1];
                if (c + b >= 0) {
                    if (c + b > 0) {
                        result.clear();
                    } else {
                        i ++;
                    }
                    if (way.compareTo(best) != 0) {
                        result.add(0, way);
                    }
                }
            }
        }
        return result;
    }
}
