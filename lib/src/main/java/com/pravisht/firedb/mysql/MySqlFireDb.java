package com.pravisht.firedb.mysql;

import com.pravisht.firedb.ConnectionConfig;
import com.pravisht.firedb.FireDb;

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
