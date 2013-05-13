package com.zudin.threemachine.model;

/**
 * Zudin Sergey, 272(2)
 * 10.02.13
 */
public class Detail {
    private int[] times;
    private int id;
    private static int counter = 1;

    /**
     * Public constructor
     * @param args times of detail
     */
    public Detail (int... args) {
        if (args.length != 3) { //only for 3 operations
            throw new IllegalArgumentException();
        }
        times = args;
        id = counter;
        counter ++;
    }

    /**
     * Return times of detail
     * @return int[] with times
     */
    public int[] getTimes () {
        return times;
    }

    /**
     * Return id of detail
     * @return id number
     */
    public int getId () {
        return id;
    }

    /**
     * Set counter to initial value
     */
    public static void reset () {
        counter = 1;
    }

    public static void setCounter (int i) {
        counter = i;
    }
}
