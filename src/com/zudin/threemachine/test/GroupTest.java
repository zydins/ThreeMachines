package com.zudin.threemachine.test;

import junit.framework.TestCase;
import com.zudin.threemachine.model.Detail;
import com.zudin.threemachine.model.Way;
import com.zudin.threemachine.model.Group;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 16.04.13
 */
public class GroupTest extends TestCase {
    public void testGetBestWay() throws Exception {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 3};
        int[] arr3 = {2, 2, 2};
        int[] arr4 = {2, 4, 2};
        Detail det1 = new Detail(arr1);
        Detail det2 = new Detail(arr2);
        Detail det3 = new Detail(arr3);
        Detail det4 = new Detail(arr4);

        Way way1 = new Way(det1);
        way1.addDetail(det2);
        way1.addDetail(det3);
        way1.addDetail(det4);
        for (int i : way1.getTimes()) {
            System.out.println(i);
        }
        System.out.println();
        Way way2 = new Way(det1);
        way2.addDetail(det4);
        way2.addDetail(det2);
        way2.addDetail(det3);
        for (int i : way2.getTimes()) {
            System.out.println(i);
        }
        System.out.println();
        Way way3 = new Way(det1);
        way3.addDetail(det3);
        way3.addDetail(det4);
        way3.addDetail(det2);
        for (int i : way3.getTimes()) {
            System.out.println(i);
        }
        System.out.println();
        Way way4 = new Way(det4);
        way4.addDetail(det2);
        way4.addDetail(det3);
        way4.addDetail(det1);
        for (int i : way4.getTimes()) {
            System.out.println(i);
        }
        System.out.println();
        Group group = new Group();
        group.add(way1);
        group.add(way2);
        group.add(way3);
        group.add(way4);
        ArrayList<Way> ways = group.getBestWay();
        for (Way way : ways) {
            if (!(way.equals(way2) || way.equals(way3))) {
                assertTrue(false);
            }
        }
        assertTrue(true);
    }
}
