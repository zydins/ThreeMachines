package com.zudin.threemachine.test;

import junit.framework.TestCase;
import com.zudin.threemachine.methods.Methods;
import com.zudin.threemachine.model.Detail;
import com.zudin.threemachine.model.Way;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 16.04.13
 */
public class MethodsTest extends TestCase {
    public void testGetSolution() throws Exception {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 3};
        int[] arr3 = {2, 2, 2};
        int[] arr4 = {2, 4, 2};
        Detail det1 = new Detail(arr1);
        Detail det2 = new Detail(arr2);
        Detail det3 = new Detail(arr3);
        Detail det4 = new Detail(arr4);
        ArrayList<Detail> details = new ArrayList<Detail>();
        details.add(det1);
        details.add(det2);
        details.add(det3);
        details.add(det4);
        Way result = Methods.getSolution(details);
        for (int i = 0; i < 3; i ++) {
            System.out.println(result.getTimes()[i]);
        }
        if (result.getTimes()[0] != 8
                || result.getTimes()[1] != 11 || result.getTimes()[2] != 14) {
            assertTrue(false);
        }
        assertTrue(true);
    }
}
