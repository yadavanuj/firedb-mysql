package com.github.yadavanuj.firedb.mysql.core;

import com.github.yadavanuj.firedb.protocol.FireDbException;
import com.github.yadavanuj.firedb.protocol.Packet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PacketMapper implements RowMapper<Packet> {
    @Override
    public Packet mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            final String json = rs.getString(1);
            return SerDe.getInstance().from(json);
        } catch (FireDbException e) {
            throw new RuntimeException(e);
        }
    }
}
