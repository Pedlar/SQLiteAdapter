package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.annotations.ColumnName;
import org.notlocalhost.sqliteadapter.annotations.TableName;

/**
 * Created by pedlar on 9/1/14.
 */
@TableName("TestTable")
public class TestTable implements Schema {

    public TestTable() {
        // Default empty constructor
    }

    @ColumnName("test_column1")
    public String test_column1;

    @ColumnName("test_column2")
    public int test_column2;

    @ColumnName("test_column3")
    public long test_column3;
}
