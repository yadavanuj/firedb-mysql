package com.pravisht.firedb.mysql;

import com.pravisht.firedb.ConnectionConfig;
import com.pravisht.firedb.FireDb;

import java.util.ArrayList;
import java.util.List;

public class MySqlFireDbProvider implements FireDb.FireDbProvider {
    @Override
    public FireDb getDriver(ConnectionConfig<?> connectionConfig) {
        final List<RpcConfig> rpcConfigs = (List<RpcConfig>) connectionConfig.getRpcConfigs();

        final ConnectionConfig<RpcConfig> config = ConnectionConfig.<RpcConfig>builder()
                .rpcExecutor(connectionConfig.getRpcExecutor())
                .providerName(connectionConfig.getProviderName())
                .rpcObserver(connectionConfig.getRpcObserver())
                .rpcConfigs(rpcConfigs)
                .build();
        return new MySqlFireDb(config);
    }
}
