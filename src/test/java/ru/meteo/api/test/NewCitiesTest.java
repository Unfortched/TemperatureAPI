package ru.meteo.api.test;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class NewCitiesTest {

    private MapDB mapDB;

    private Service service;

    @Test
    public void main() {

        mapDB = new MapDB();
        service = new Service(mapDB);

        String result = service.getInfo("Chicago/US", new Date(0L)).encode();
        assertTrue(result.contains("404"));

        String result2 = service.getInfo("Chi", new Date(0L)).encode();
        assertTrue(result2.contains("404"));

        String result3 = service.getInfo("Moscow", new Date(0L)).encode();
        assertTrue(result3.contains("info"));


        MeteoData meteoData = MeteoData.builder().city("Test/Test").temp(12.5).build();
        mapDB.insertRecord(2L, meteoData);
        assertEquals(12.5, Double.parseDouble(mapDB.getCache().get(2L).split("=")[1]));

        MeteoData meteoData1 = MeteoData.builder().city("Test1/Tes1t1").temp(15.5).build();
        mapDB.insertRecord(2L, meteoData1);
        assertEquals(15.5, Double.parseDouble(mapDB.getCache().get(2L).split("=")[1]));

    }
}
