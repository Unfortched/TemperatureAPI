package ru.meteo.api.test;

import org.mapdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MapDB {
    private static final Logger log = LoggerFactory.getLogger(MapDB.class);
    private static DB db;
    public static HTreeMap<Long, String> cache;

    @Value("${cities}")
    public String configCities;

    public MapDB() {
        DBMaker.Maker builder = DBMaker
                .memoryDB()
                .allocateStartSize(500 * 1024 * 1024)
                .allocateIncrement(100 * 1024 * 1024)
                .closeOnJvmShutdown()
                .checksumHeaderBypass();

        try {
            db = builder.make();
        } catch (Throwable e) {
            log.warn("create db error {}", e.getMessage());
            try {
                db = builder.make();
            } catch (Throwable e2) {
                log.error("create db error", e);
                log.info(e2.getMessage());
                throw e2;
            }
        }

        cache = db.hashMap("cache", Serializer.LONG, Serializer.STRING)
                .counterEnable()
                .expireExecutor(new ScheduledThreadPoolExecutor(1))
                .createOrOpen();
        commit();
        log.info("Db init");
    }

    @PostConstruct
    public void uploadCities() {

        if(configCities.isEmpty() || configCities.contains(",,")){
            log.error("Config cities string is empty or it have empty city/country value");
            System.exit(-1);
        }


        List<String> cities = Arrays.asList(configCities.split(","));

        for (String city : cities) {
            MeteoData meteoData = MeteoData
                    .builder()
                    .city(city)
                    .temp(0)
                    .build();

            insertRecord((long) cities.indexOf(city), meteoData);
        }
        commit();
    }

    public HTreeMap<Long, String> getCache() {
        return cache;
    }

    public void commit() {
        db.getStore().compact();
    }

    public void insertRecord(Long timestamp, MeteoData data) {
        cache.put(timestamp, data.toString());
    }

}
