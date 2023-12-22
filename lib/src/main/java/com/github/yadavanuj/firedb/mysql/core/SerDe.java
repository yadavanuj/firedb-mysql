package com.github.yadavanuj.firedb.mysql.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yadavanuj.firedb.protocol.FireDbException;
import com.github.yadavanuj.firedb.protocol.Packet;

// TODO: Inject serde
public class SerDe {
    private static final SerDe INSTANCE = new SerDe();
    private final ObjectMapper objectMapper;
    private SerDe() {
        this(new ObjectMapper());
    }
    private SerDe(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    public static SerDe getInstance() {
        return INSTANCE;
    }
    public Packet from(String jsonString) throws FireDbException {
        try {
            return objectMapper.readValue(jsonString, Packet.class);
        } catch (JsonProcessingException e) {
            throw new FireDbException(e);
        }
    }

    public String serialize(Packet packet) throws FireDbException {
        try {
            return this.objectMapper.writeValueAsString(packet);
        } catch (JsonProcessingException e) {
            throw new FireDbException(e);
        }
    }
}
