package com.xixi;

import junit.framework.TestSuite;


/**
 * TestSuite用于管理TestCase
 */
public class MyTestSuite extends TestSuite {
    public MyTestSuite() {
        addTestSuite(MainTest.class );
    }
}
