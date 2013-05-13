package com.zudin.threemachine.test;

import junit.framework.TestCase;
import com.zudin.threemachine.model.Detail;
import com.zudin.threemachine.model.Way;

/**
 * Zudin Sergey, 272(2)
 * 16.04.13
 */
public class WayTest extends TestCase {
    public void testAddDetail1() throws Exception {
        int[] arr1 = {1, 3, 3};
        int[] arr2 = {2, 2, 2};
        Way way = new Way(new Detail(arr1));
        way.addDetail(new Detail(arr2));
        if (way.getTimes()[0] != 3 && way.getTimes()[1] != 6 && way.getTimes()[2] != 9) {
            assertTrue(false);
        }
        assertTrue(true);
    }

    public void testAddDetail2() throws Exception {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 3};
        int[] arr3 = {2, 2, 2};
        int[] arr4 = {2, 4, 2};
        Detail det1 = new Detail(arr1);
        Detail det2 = new Detail(arr2);
        Detail det3 = new Detail(arr3);
        Detail det4 = new Detail(arr4);
        Way way = new Way(det1);
        way.addDetail(det3);
        for (int i : way.getTimes()) {
            System.out.println(i);
        }
        if (way.getTimes()[0] != 3 || way.getTimes()[1] != 5 || way.getTimes()[2] != 8) {
            assertTrue(false);
        }
        assertTrue(true);
    }

    public void testIsDetailsEquals() throws Exception {

    }

    public void testCompareTo() throws Exception {

    }
}
