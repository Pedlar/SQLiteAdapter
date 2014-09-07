package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.annotations.Column;
import org.notlocalhost.sqliteadapter.annotations.TableName;

/**
 * Created by pedlar on 9/6/14.
 */
@TableName("TestForeignTable")
public class TestForeignTable {
    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;
}
