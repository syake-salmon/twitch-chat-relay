package com.syakeapps.tcr.dao;

public class DaoBase {
    protected final String DRIVER_NAME = "org.sqlite.JDBC";
    protected String dbUrl;

    public DaoBase() {
        this("jdbc:sqlite:database.db");
    }

    public DaoBase(String dbUrl) {
        this.dbUrl = dbUrl;

        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver: " + DRIVER_NAME, e);
        }
    }
}
