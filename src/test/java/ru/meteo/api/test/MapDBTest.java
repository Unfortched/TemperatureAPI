package ru.meteo.api.test;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapDBTest {

    @Test
    public void main() {
        MapDB mapDB = new MapDB();
        Service service = new Service(mapDB);

        mapDB.insertRecord(1L, MeteoData.parse("Moscow/Russia=23.0"));
        mapDB.insertRecord(2L, MeteoData.parse("Tokio/Japan=25.0"));
        mapDB.insertRecord(3L, MeteoData.parse("Moscow/Russia=27.0"));
        mapDB.insertRecord(4L, MeteoData.parse("Moscow/Russia=28.0"));
        mapDB.insertRecord(5L, MeteoData.parse("Moscow/Russia=22.0"));
        mapDB.insertRecord(6L, MeteoData.parse("Moscow/Russia=23.0"));
        mapDB.insertRecord(7L, MeteoData.parse("Moscow/Russia=25.0"));

        assertNotNull(service.getDataByTimestamp("Moscow", 0L));

        Map<Long, MeteoData> lastData = new HashMap<>();
        lastData.put(7L, MeteoData.parse("Moscow/Russia=25.0"));

        assertEquals(lastData.toString(), service.getDataByTimestamp("Moscow", 0L).toString());

        assertTrue(service.getInfo("Paris/France", new Date(0L)).encode().contains("Selected city not configured"));

    }
}
