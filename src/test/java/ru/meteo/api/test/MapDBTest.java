package ru.meteo.api.test;

import java.util.Date;

public class MapDBTest {
    public static void main(String[] args) {
        MapDB mapDB = new MapDB();
        Service service = new Service(mapDB);

        mapDB.insertRecord(1L, MeteoData.parse("Moscow/Russia=23"));
        mapDB.insertRecord(2L, MeteoData.parse("Tokio/Japan=25"));
        mapDB.insertRecord(3L, MeteoData.parse("Moscow/Russia=27"));
        mapDB.insertRecord(4L, MeteoData.parse("Moscow/Russia=28"));
        mapDB.insertRecord(5L, MeteoData.parse("Moscow/Russia=22"));
        mapDB.insertRecord(6L, MeteoData.parse("Moscow/Russia=23"));
        mapDB.insertRecord(7L, MeteoData.parse("Moscow/Russia=25"));

        System.out.println(service.getDataByTimestamp("Moscow", 5L));
        System.exit(0);

    }
}
