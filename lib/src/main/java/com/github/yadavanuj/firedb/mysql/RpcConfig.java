package com.github.yadavanuj.firedb.mysql;

import com.github.yadavanuj.firedb.protocol.Packet;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class RpcConfig {
    private Packet packet;
    private String procedureName;
    @Builder.Default
    @NonNull
    private PollingTaskExecutor.PollerConfig pollerConfig = PollingTaskExecutor.PollerConfig.builder().build();
}
