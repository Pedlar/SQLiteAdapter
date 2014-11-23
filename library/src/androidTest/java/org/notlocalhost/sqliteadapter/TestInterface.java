package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.annotations.Column;
import org.notlocalhost.sqliteadapter.annotations.DELETE;
import org.notlocalhost.sqliteadapter.annotations.INSERT;
import org.notlocalhost.sqliteadapter.annotations.QUERY;
import org.notlocalhost.sqliteadapter.annotations.TableName;
import org.notlocalhost.sqliteadapter.annotations.UPDATE;
import org.notlocalhost.sqliteadapter.annotations.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedlar on 8/31/14.
 */
public interface TestInterface {
    @QUERY
    @TableName("TestTable")
    public TestTable getTestData(@Where("test_column1") String testData);

    @QUERY
    @TableName("TestTable")
    public ArrayList<TestTable> getTestDataList(@Where("test_column1") String testData);

    @INSERT
    @TableName("TestTable")
    public long setTestData(@Column(name = "test_column1") String testData,
                            @Column(name = "test_column2") int testInt,
                            @Column(name = "test_column3") long testLong);

    @UPDATE
    @TableName("TestTable")
    public long updateTestData(@Column(name = "test_column1") String testData,
                               @Column(name = "test_column2") int testInt,
                               @Column(name = "test_column3") long testLong);

    @INSERT
    @TableName(Constants.FOREIGN_PREFIX + "TestForeignTable")
    public long setTestForeignCol4(@Column(name = Constants.FOREIGN_PREFIX + "id") long foreignKey,
                                   @Column(name = Constants.FOREIGN_PREFIX + "column") String column,
                                   @Column(name = "name") String myName,
                                   @Column(name = "description") String myDesc);

    @INSERT
    @TableName(Constants.FOREIGN_PREFIX + "TestForeignTable")
    public long setTestForeignCol5(@Column(name = Constants.FOREIGN_PREFIX + "id") long foreignKey,
                                   @Column(name = Constants.FOREIGN_PREFIX + "column") String column,
                                   @Column(name = "name") String myName,
                                   @Column(name = "description") String myDesc);


    @INSERT
    @TableName("TestTable")
    public long setTestForeign(@Column(name = "test_column4", foreignKey = true) TestForeignTable foreignTable,
                               @Column(name = "test_column1") String testData);

    @INSERT
    @TableName("TestTable")
    public long setTestForeignList(@Column(name = "test_column5", foreignKey = true) ArrayList<TestForeignTable> foreignTableList,
                                   @Column(name = "test_column1") String testData);

    @DELETE
    @TableName("TestTable")
    public int deleteTestData(@Where("test_column1") String testData);
}
