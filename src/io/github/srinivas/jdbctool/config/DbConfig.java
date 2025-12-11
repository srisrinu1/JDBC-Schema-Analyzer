package io.github.srinivas.jdbctool.config;

public class DbConfig {
    private final String url;
    private final String username;
    private final String password;
    private final String driverClass;
    private final String dialectName;
    public DbConfig(String url, String username, String password, String driverClass, String dialectName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
        this.dialectName = dialectName;
    }
    public String getUrl() {
        return url;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getDriverClass() {
        return driverClass;
    }
    public String getDialectName() {
        return dialectName;
    }
    @Override
    public String toString() {
        return "DbConfig{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", driverClass='" + driverClass + '\'' +
                ", dialectName='" + dialectName + '\'' +
                '}';
    }

}
