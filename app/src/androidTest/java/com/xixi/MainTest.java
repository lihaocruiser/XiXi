package com.xixi;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * AndroidTestCase可以run
 */
public class MainTest extends AndroidTestCase {

    protected int i1;
    protected int i2;
    static final String LOG_TAG = "MainTest";

    public void setUp() {
        i1 = 2;
        i2 = 3;
    }

    public void testAdd() {
        Log.d(LOG_TAG, "testAdd");
        assertTrue( LOG_TAG + "1", ( ( i1 + i2 ) == 4 ) );
    }

    @Override
    public void testAndroidTestCaseSetupProperly() {
        super.testAndroidTestCaseSetupProperly();
        Log.d( LOG_TAG, "testAndroidTestCaseSetupProperly" );
    }

}
