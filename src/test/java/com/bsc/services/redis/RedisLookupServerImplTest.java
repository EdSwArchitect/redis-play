package com.bsc.services.redis;

import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class RedisLookupServerImplTest {

    @Test
    public void connect() {
        RedisTables.LOOKUP_TBL f;

        for (RedisTables.LOOKUP_TBL t : RedisTables.LOOKUP_TBL.values()) {
            System.out.format("Value: %s Ordinal %d%n",t, t.ordinal());
        }

    }

    @Test
    public void disconnect() {
    }
}