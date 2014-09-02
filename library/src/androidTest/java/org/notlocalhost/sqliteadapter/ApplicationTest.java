package org.notlocalhost.sqliteadapter;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testAdapterInsertQuery() {
        createApplication();
        SQLiteAdapter adapter = new SQLiteAdapter.Builder(getContext())
                .setSchemas(TestTable.class)
                .setDatabaseName("test_db" + new Random(13233324).nextLong() + ".db")
                .build();
        TestInterface testInterface = adapter.create(TestInterface.class);
        int testInt = 42;
        long testLong = 424242424242l;
        long id = testInterface.setTestData("TestData1", testInt, testLong);

        assertEquals(1, id);

        TestTable testData = testInterface.getTestData("TestData1");
        assertEquals("TestData1", testData.test_column1);
        assertEquals(42, testData.test_column2);
        assertEquals(424242424242l, testData.test_column3);
    }

}