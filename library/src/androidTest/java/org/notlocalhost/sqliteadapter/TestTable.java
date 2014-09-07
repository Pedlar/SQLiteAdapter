package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.annotations.Column;
import org.notlocalhost.sqliteadapter.annotations.TableName;

import java.util.ArrayList;

/**
 * Created by pedlar on 9/1/14.
 */
@TableName("TestTable")
public class TestTable implements Schema {

    public TestTable() {
        // Default empty constructor
    }

    @Column(name = "test_column1")
    public String test_column1;

    @Column(name = "test_column2")
    public int test_column2;

    @Column(name = "test_column3")
    public long test_column3;

    @Column(name = "test_column4", foreignKey = true)
    public TestForeignTable foreignTable;

    @Column(name = "test_column5", foreignKey = true)
    public ArrayList<TestForeignTable> foreignTableList;
}
