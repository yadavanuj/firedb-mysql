package com.pravisht.firedb.mysql;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MySqlExecutionContext {
    private String procedureName;
}
