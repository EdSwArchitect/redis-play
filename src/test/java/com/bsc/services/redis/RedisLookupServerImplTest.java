package com.bsc.services.redis;

import com.bsc.services.LookupServer;
import com.bsc.services.LookupServerFactory;
import com.bsc.services.LookupServiceException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisLookupServerImplTest {
    Logger log = Logger.getLogger(RedisLookupServerImplTest.class.getName());

    @Test
    public void connect() {
        RedisTables.LOOKUP_TBL f;

        for (RedisTables.LOOKUP_TBL t : RedisTables.LOOKUP_TBL.values()) {
            String l = String.format("Value: %s Ordinal %d%n", t, t.ordinal());

            log.info(l);
        }

    }

    @Test
    public void zdisconnect() {
        log.info("Disconnect");
    }

    @Test
    public void test0() {
        try {
            LookupServer server = LookupServerFactory.getLookupServer();
            server.connect("redis://localhost");

            String info = server.getInfo();

            Assert.assertNotNull("Info should not be null", info);

            String l = String.format("%s", info);

            log.info(l);

        } catch (LookupServiceException lse) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void test1() {
        try {
            LookupServer server = LookupServerFactory.getLookupServer();
            server.connect("redis://localhost");

            Map<String, String> map = new HashMap<String, String>();

            map.put("last", "Brown");
            map.put("first", "Edwin");
            map.put("address", "4825 Royal Coachman Dr");
            map.put("city", "Elkridge");
            map.put("state", "Maryland");
            map.put("zipCode", "21075");

            server.set(RedisTables.LOOKUP_TBL.TBL_2.toString(), "Edwin Brown", map);


        } catch (LookupServiceException lse) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void test2() {
        try {
            LookupServer server = LookupServerFactory.getLookupServer();
            server.connect("redis://localhost");

            Map<String, String> map = server.getHash(RedisTables.LOOKUP_TBL.TBL_2.toString(), "Edwin Brown");

            Assert.assertNotNull("Unable to get the map for Edwin Brown", map);

            String l = String.format("The map returned: %s", map);

            log.info(l);

        } catch (LookupServiceException lse) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void test3() {
        try {

            log.info("Sabrina, Jayden, and Janet Jackson added");

            LookupServer server = LookupServerFactory.getLookupServer();
            server.connect("redis://localhost");

            Map<String, String> map = new HashMap<String, String>();

            map.put("last", "Brown");
            map.put("first", "Sabrina");
            map.put("address", "4825 Royal Coachman Dr");
            map.put("city", "Elkridge");
            map.put("state", "Maryland");
            map.put("zipCode", "21075");

            server.set(RedisTables.LOOKUP_TBL.TBL_2.toString(), "Sabrina Brown", map);

            map.put("last", "Brown");
            map.put("first", "Jayden");
            map.put("address", "4825 Royal Coachman Dr");
            map.put("city", "Elkridge");
            map.put("state", "Maryland");
            map.put("zipCode", "21075");

            server.set(RedisTables.LOOKUP_TBL.TBL_2.toString(), "Jayden Brown", map);

            map.put("last", "Jackson");
            map.put("first", "Janet");
            map.put("address", "2300 Jackson Street");
            map.put("city", "Los Angeles");
            map.put("state", "California");
            map.put("zipCode", "");

            server.set(RedisTables.LOOKUP_TBL.TBL_2.toString(), "Janet Jackson", map);
        } catch (LookupServiceException lse) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void test4() {
        try {
            LookupServer server = LookupServerFactory.getLookupServer();
            server.connect("redis://localhost");

            List<String> list = server.getKeys(RedisTables.LOOKUP_TBL.TBL_2.toString());

            Assert.assertNotNull("I expected keys", list);

            Assert.assertTrue("Expected the list to be greater than zero", list.size() > 0);

            String l = String.format("Here are the keys: %s", list);

            log.info(l);

            l  = String.format("List size: %d", list.size());

            log.info(l);


        } catch (LookupServiceException lse) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }

    }


    @Test
    public void test5() {
        try {
            LookupServer server = LookupServerFactory.getLookupServer();
            server.connect("redis://localhost");

            int size = server.getKeys(RedisTables.LOOKUP_TBL.TBL_2.toString()).size();

            server.delete(RedisTables.LOOKUP_TBL.TBL_2.toString(), "Jayden Brown");

            int newSize = server.getKeys(RedisTables.LOOKUP_TBL.TBL_2.toString()).size();

            String l = String.format("Old size: %d. New size: %d", size, newSize);

            log.info(l);

            Assert.assertTrue("Size should be off by one", newSize +1 == size);

            server.delete(RedisTables.LOOKUP_TBL.TBL_2.toString(), "Sabrina Brown", "Edwin Brown");

            newSize = server.getKeys(RedisTables.LOOKUP_TBL.TBL_2.toString()).size();

            l = String.format("Old size: %d. New size: %d", size, newSize);

            log.info(l);

            Assert.assertTrue("Size should be off by three", newSize +3 == size);


        } catch (LookupServiceException lse) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }

    }

}