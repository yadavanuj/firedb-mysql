package com.github.yadavanuj.firedb.mysql;

import com.github.yadavanuj.firedb.FireDb;
import com.github.yadavanuj.firedb.protocol.FireDbException;
import com.github.yadavanuj.firedb.protocol.Packet;
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
