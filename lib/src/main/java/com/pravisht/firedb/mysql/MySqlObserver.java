package com.pravisht.firedb.mysql;

import com.pravisht.firedb.FireDb;
import com.pravisht.firedb.protocol.FireDbException;
import com.pravisht.firedb.protocol.Packet;
import org.javatuples.Pair;

import java.util.function.Consumer;

public class MySqlObserver implements FireDb.Observer {
    private final Consumer<Pair<Packet, FireDbException>> consumer;

    public MySqlObserver(Consumer<Pair<Packet, FireDbException>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void handle(Pair<Packet, FireDbException> result) {
        this.consumer.accept(result);
    }
}
