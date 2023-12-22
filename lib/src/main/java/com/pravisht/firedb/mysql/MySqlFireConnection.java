package com.pravisht.firedb.mysql;

import com.pravisht.firedb.ConnectionConfig;
import com.pravisht.firedb.FireDb;
import com.pravisht.firedb.protocol.FireDbException;
import com.pravisht.firedb.protocol.Packet;
import org.javatuples.Pair;

import java.util.Objects;

// TODO: Refactor
public class MySqlFireConnection extends FireDb.BaseConnection<RpcConfig> {
    public MySqlFireConnection(ConnectionConfig<RpcConfig> mysqlConnectionConfig) {
        super(mysqlConnectionConfig);
        this.startListener();
    }

    private void startListener() {
        final ConnectionConfig<RpcConfig> connectionConfig = config;
        // TODO: Do Better
        for (RpcConfig rpcConfig: connectionConfig.getRpcConfigs()) {
            runTask(connectionConfig.getRpcExecutor(), connectionConfig.getRpcObserver(), rpcConfig);
        }
    }

    private static void runTask(FireDb.Executor executor, FireDb.Observer observer, RpcConfig rpcConfig) {
        // Create Runnable
        if (!Objects.isNull(rpcConfig.getPacket())) {
            Runnable task = new Runnable() {
                Pair<Packet, FireDbException> result = new Pair<>(null, null);
                @Override
                public void run() {
                    final Packet packet;
                    final MySqlExecutionContext mySqlExecutionContext = MySqlExecutionContext.builder()
                            .procedureName(rpcConfig.getProcedureName())
                            .build();
                    final FireDb.ExecutionContext<MySqlExecutionContext> executionContext = FireDb.ExecutionContext.<MySqlExecutionContext>builder()
                            .props(mySqlExecutionContext)
                            .build();
                    try {
                        packet = executor.execute(rpcConfig.getPacket(), executionContext);
                        result = result.setAt0(packet);
                    } catch (FireDbException e) {
                        result = result.setAt1(e);
                    }
                    observer.handle(result);
                }
            };

            final PollingTaskExecutor.PollerConfig pollerConfig = rpcConfig.getPollerConfig();
            final PollingTaskExecutor pollingTaskExecutor = PollingTaskExecutor.from(pollerConfig, task);
            pollingTaskExecutor.start();
        }
    }
}
