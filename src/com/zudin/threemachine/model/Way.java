package com.zudin.threemachine.model;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 10.02.13
 */
public class Way implements Comparable<Way> {
    private ArrayList<Detail> details = new ArrayList<Detail>();
    private int[] times;

    /**
     * Public constructor
     * @param detail first detail in queue
     */
    public Way (Detail detail) {
        if (detail == null) {
            throw new IllegalArgumentException();
        }
        details.add(detail);
        times = new int[detail.getTimes().length];
        times[0] = detail.getTimes()[0];
        times[1] = times[0] + detail.getTimes()[1];
        times[2] = times[1] + detail.getTimes()[2];
    }

    /**
     * Public constructor
     * @param way way to copy
     */
    public Way (Way way) {
        details.addAll(way.getDetails());
        times = way.getTimes().clone();
    }

    /**
     * Adding detail in the way
     * @param detail detail which have to add in the way
     */
    public void addDetail (Detail detail) {
        if (detail.getTimes().length != times.length) {
            throw new IllegalArgumentException();
        }
        details.add(detail);
        times[0] += detail.getTimes()[0];
        for (int i = 1; i < times.length; i ++) {
            int delta = times[i - 1] > times[i] ? times[i - 1] - times[i] : 0; //if there is a gap between times
            times[i] += delta + detail.getTimes()[i];
        }
    }

    /**
     * Returns list with details
     * @return ArrayList with details
     */
    public ArrayList<Detail> getDetails () {
        return details;
    }

    /**
     * Returns array with table of times of all details
     * @return array with times of details
     */
    public int[][] getFullTimes () {
        int[][] result = new int [details.size() + 2][3];
        for (int i = 0; i < details.size(); i ++) {
            result[i][0] = details.get(i).getTimes()[0];
            result[i + 1][1] = details.get(i).getTimes()[1];
            result[i + 2][2] = details.get(i).getTimes()[2];
        }
        return result;
    }

    /**
     * Returns array with times of the way
     * @return int[] with times of the way
     */
    public int[] getTimes () {
        return times;
    }

    /**
     * Checks if 2 way has the same details in the queue.
     * @param way way to compare
     * @return "true" if ways has the same details, "false" otherwise
     */
    public boolean isDetailsEquals (Way way) {
        if (way.getDetails().size() != details.size()) {
            return false;
        }
        for (Detail tempDetail : way.getDetails()) {
            if (!details.contains(tempDetail)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares this object with the specified object for order.
     * @param way way to compare
     * @return a negative integer, zero, or a positive integer as this way
     *          is less than, equal to, or greater than the specified way.
     */
    public int compareTo (Way way) {
        if (!this.isDetailsEquals(way)) {
            throw new IllegalArgumentException();
        }
        int[] compared = way.getTimes();
        for (int i = compared.length - 1; i >= 0; i --) {
            if (compared[i] > times[i]) {
                return -1;
            } else {
                if ((compared[i] < times[i])) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * Check way for presence of specified detail
     * @param detail detail to find
     * @return true if way contains this detail, false otherwise
     */
    public boolean contains (Detail detail) {
        for (Detail temp : details) {
            if (temp.equals(detail)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    public String toString () {
        String result = "";
        for (Detail detail : details) {
            result += detail.getId() + " ";
        }
        result += ": [ ";
        for (int time : times) {
            result += time + " ";
        }
        result += "]";
        return result;
    }
}