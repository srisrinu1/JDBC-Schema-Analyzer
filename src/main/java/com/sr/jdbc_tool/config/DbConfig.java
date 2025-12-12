package com.sr.jdbc_tool.config;

public class DbConfig {
    private final String url;
    private final String userName;
    private final String password;
    private final String driverClassName;
    private final String dialect;
    public DbConfig(String url, String userName, String password, String driverClassName, String dialect) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.driverClassName = driverClassName;
        this.dialect = dialect;
    }
    public String getUrl() {
        return url;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public String getDriverClassName() {
        return driverClassName;
    }
    public String getDialect() {
        return dialect;
    }
    @Override 
    public String toString() {
            return "DbConfig {url=" + url + ", userName=" + userName + ", driverClassName=" + driverClassName
                + ", dialect=" + dialect + "}";
    }

}
