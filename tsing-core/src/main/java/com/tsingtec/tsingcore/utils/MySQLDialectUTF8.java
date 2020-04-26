package com.tsingtec.tsingcore.utils;

import org.hibernate.dialect.MySQL5Dialect;

public class MySQLDialectUTF8 extends MySQL5Dialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}