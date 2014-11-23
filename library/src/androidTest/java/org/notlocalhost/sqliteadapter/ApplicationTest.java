package org.notlocalhost.sqliteadapter;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    final String databaseName = "test_db" + new Random(13233324).nextLong() + ".db";
    SQLiteAdapter adapter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
        adapter = new SQLiteAdapter.Builder(getContext())
                .setSchemas(TestTable.class)
                .setDatabaseName(databaseName)
                .build();
    }

    @Override
    public void tearDown() throws Exception {
        Context context = getContext().getApplicationContext();
        if(context != null) {
            context.deleteDatabase(databaseName);
        }
        super.tearDown();
    }

    public void testAdapterInsertQuery() {
        TestInterface testInterface = adapter.create(TestInterface.class);
        String col1 = "TestData1";
        int testInt = 42;
        long testLong = 424242424242l;
        testInterface.setTestData(col1, testInt, testLong);

        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertEquals(testInt, testData.test_column2);
        assertEquals(testLong, testData.test_column3);
    }

    public void testAdapterUpdateQuery() {
        TestInterface testInterface = adapter.create(TestInterface.class);
        String col1 = "TestData1";
        int testInt = 42;
        long testLong = 424242424242l;
        testInterface.setTestData(col1, testInt, testLong);

        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertEquals(testInt, testData.test_column2);
        assertEquals(testLong, testData.test_column3);

        testInt += 2;
        testLong += 2;
        col1 = "TestData42";

        testInterface.updateTestData(col1, testInt, testLong);
        TestTable testData2 = testInterface.getTestData(col1);
        assertEquals(col1, testData2.test_column1);
        assertEquals(testInt, testData2.test_column2);
        assertEquals(testLong, testData2.test_column3);

    }

    /* This test is just to make sure Foreign Keys are working at a low level */
    public void testAdapterQueryWithForeignDirect() {
        TestInterface testInterface = adapter.create(TestInterface.class);
        String col1 = "TestData1";
        String myName = "My Name";
        String myDesc = "My Desc";
        String col4 = "test_column4";
        int testInt = 42;
        long testLong = 424242424242l;
        long id = testInterface.setTestData(col1, testInt, testLong);

        testInterface.setTestForeignCol4(id, col4, myName, myDesc);

        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertEquals(testInt, testData.test_column2);
        assertEquals(testLong, testData.test_column3);
        assertNotNull(testData.foreignTable);
        assertEquals(myName, testData.foreignTable.name);
        assertEquals(myDesc, testData.foreignTable.description);
    }

    public void testAdapterQueryWithForeign() {
        String foreignTableName = "My Foreign Table Test";
        String col1 = "TestData2";
        TestInterface testInterface = adapter.create(TestInterface.class);

        TestForeignTable foreignTable = new TestForeignTable();
        foreignTable.name = foreignTableName;

        testInterface.setTestForeign(foreignTable, col1);

        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertNotNull(testData.foreignTable);
        assertEquals(foreignTableName, testData.foreignTable.name);
    }


    public void testAdapterDelete() {
        String col1 = "TestDataDeleteMe";
        TestInterface testInterface = adapter.create(TestInterface.class);
        int testInt = 42;
        long testLong = 42l;
        long id = testInterface.setTestData(col1, testInt, testLong);

        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertEquals(42, testData.test_column2);
        assertEquals(42, testData.test_column3);

        int colAffected = testInterface.deleteTestData(col1);

        assertEquals(1, colAffected);

        TestTable testData1 = testInterface.getTestData(col1);
        assertNull(testData1);
    }

    public void testAdapterQueryList() {
        String col1 = "TestData1";
        TestInterface testInterface = adapter.create(TestInterface.class);
        testInterface.setTestData(col1, 42, 42l);
        testInterface.setTestData(col1, 43, 43l);
        testInterface.setTestData(col1, 44, 44l);

        ArrayList<TestTable> testData = testInterface.getTestDataList(col1);
        assertEquals(3, testData.size());
    }


    /* This test is just to make sure Foreign Keys are working at a low level */
    public void testAdapterQueryWithForeignListDirect() {
        TestInterface testInterface = adapter.create(TestInterface.class);
        String col1 = "TestData1";
        String myName = "My Name";
        String myDesc = "My Desc";
        String col5 = "test_column5";
        int testInt = 42;
        long testLong = 424242424242l;
        long id = testInterface.setTestData(col1, testInt, testLong);

        testInterface.setTestForeignCol5(id, col5, myName + "0", myDesc);
        testInterface.setTestForeignCol5(id, col5, myName + "1", myDesc);
        testInterface.setTestForeignCol5(id, col5, myName + "2", myDesc);
        testInterface.setTestForeignCol5(id, col5, myName + "3", myDesc);


        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertEquals(testInt, testData.test_column2);
        assertEquals(testLong, testData.test_column3);
        assertNotNull(testData.foreignTableList);
        assertEquals(4, testData.foreignTableList.size());
    }

    /* This test is just to make sure Foreign Keys are working at a low level */
    public void testAdapterQueryWithForeignListAndForeignDirect() {
        TestInterface testInterface = adapter.create(TestInterface.class);
        String col1 = "TestData1";
        String myName = "My Name";
        String myDesc = "My Desc";
        String col5 = "test_column5";
        String col4 = "test_column4";
        int testInt = 42;
        long testLong = 424242424242l;
        long id = testInterface.setTestData(col1, testInt, testLong);

        testInterface.setTestForeignCol4(id, col4, myName, myDesc);
        testInterface.setTestForeignCol5(id, col5, myName + "0", myDesc);
        testInterface.setTestForeignCol5(id, col5, myName + "1", myDesc);
        testInterface.setTestForeignCol5(id, col5, myName + "2", myDesc);
        testInterface.setTestForeignCol5(id, col5, myName + "3", myDesc);


        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertEquals(testInt, testData.test_column2);
        assertEquals(testLong, testData.test_column3);
        assertNotNull(testData.foreignTableList);
        for(int i = 0; i < testData.foreignTableList.size(); i++) {
            assertEquals(myName + Integer.toString(i), testData.foreignTableList.get(i).name);
        }
        assertEquals(myName, testData.foreignTable.name);
    }

    public void testAdapterQueryWithForeignList() {
        TestInterface testInterface = adapter.create(TestInterface.class);
        String col1 = "TestData1";
        String myName = "My Name";
        String myDesc = "My Desc";
        ArrayList<TestForeignTable> testForeignTables = new ArrayList<TestForeignTable>();
        for(int i = 0; i < 10; i++) {
            TestForeignTable testForeignTable = new TestForeignTable();
            testForeignTable.name = myName + Integer.toString(i);
            testForeignTable.description = myDesc;
            testForeignTables.add(testForeignTable);
        }

        testInterface.setTestForeignList(testForeignTables, col1);

        TestTable testData = testInterface.getTestData(col1);
        assertEquals(col1, testData.test_column1);
        assertNotNull(testData.foreignTableList);
        assertEquals(10, testData.foreignTableList.size());
    }
}