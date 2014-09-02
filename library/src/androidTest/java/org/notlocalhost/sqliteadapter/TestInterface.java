package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.annotations.ColumnName;
import org.notlocalhost.sqliteadapter.annotations.INSERT;
import org.notlocalhost.sqliteadapter.annotations.QUERY;
import org.notlocalhost.sqliteadapter.annotations.TableName;
import org.notlocalhost.sqliteadapter.annotations.Where;

/**
 * Created by pedlar on 8/31/14.
 */
public interface TestInterface {
    @QUERY
    @TableName("TestTable")
    public TestTable getTestData(@Where("test_column1") String testData);

    @INSERT
    @TableName("TestTable")
    public long setTestData(@ColumnName("test_column1") String testData,
                            @ColumnName("test_column2") int testInt,
                            @ColumnName("test_column3") long testLong);
}
