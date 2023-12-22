package com.pravisht.firedb.mysql;

import com.pravisht.firedb.FireDb;
import com.pravisht.firedb.mysql.core.PacketMapper;
import com.pravisht.firedb.mysql.core.SerDe;
import com.pravisht.firedb.protocol.Packet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;

public class MySqlExecutor implements FireDb.Executor {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MySqlExecutor(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    // TODO: Create custom exceptions. Handle better.
    @Override
    public Packet execute(Packet packet, FireDb.ExecutionContext<?> executionContext) throws RuntimeException {
        final FireDb.ExecutionContext<MySqlExecutionContext> context = (FireDb.ExecutionContext<MySqlExecutionContext>) executionContext;
        final String sql = String.format("call %s(:variable)", context.getProps().getProcedureName());
        try {
            final String json = SerDe.getInstance().serialize(packet);
            SqlParameterSource namedParameters = new MapSqlParameterSource("variable", json);
            return jdbcTemplate.queryForObject(sql, namedParameters, new PacketMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
