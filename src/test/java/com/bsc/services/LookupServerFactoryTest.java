package com.bsc.services;

import com.bsc.services.redis.RedisTables;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class LookupServerFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getLookupServer() {
        try {
            LookupServer lookupServer = LookupServerFactory.getLookupServer();

            Assert.assertNotNull("LookupServer object can't be null", lookupServer);

            System.out.format("The lookup server object: %s%n", lookupServer);

            lookupServer.connect("redis://localhost");

            System.out.format("The lookup server object: %s%n", lookupServer);

            System.out.format("Lookup service info:%n%s%n", lookupServer.getInfo());

            lookupServer.set(RedisTables.LOOKUP_TBL.TBL_1.toString(), "Edwin", "Brown");
            lookupServer.set(RedisTables.LOOKUP_TBL.TBL_1.toString(), "Sabrina", "Brown");
            lookupServer.set(RedisTables.LOOKUP_TBL.TBL_1.toString(), "Janet", "Jackson");


            String value = lookupServer.get(RedisTables.LOOKUP_TBL.TBL_1.toString(), "Edwin");

            System.out.format("The value for Edwin is: '%s'%n", value);

        } catch (LookupServiceException lse) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());

        }
    }

    @Test
    public void getLookupServer1() {
    }
}