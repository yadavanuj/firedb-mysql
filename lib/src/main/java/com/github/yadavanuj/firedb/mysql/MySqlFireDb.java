package com.github.yadavanuj.firedb.mysql;


import com.github.yadavanuj.firedb.ConnectionConfig;
import com.github.yadavanuj.firedb.FireDb;

public class MySqlFireDb implements FireDb {
    private final ConnectionConfig<RpcConfig> connectionConfig;

    public MySqlFireDb(ConnectionConfig<RpcConfig> connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    @Override
    public Connection getConnection() {
        return new MySqlFireConnection(connectionConfig);
    }
}
