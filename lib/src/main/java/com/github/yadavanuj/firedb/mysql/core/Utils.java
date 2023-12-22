package com.github.yadavanuj.firedb.mysql.core;

import java.util.Date;

public class Utils {
    public static java.sql.Date currentTimeStamp() {
        return new java.sql.Date(new Date().getTime());
    }
}
