package ru.meteo.api.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MeteoDataTest {

    @Test
    public void parseTest() {

        MeteoData meteoData = MeteoData.builder()
                .city("Berlin/Germany")
                .temp(15.2)
                .build();

        MeteoData meteoData1 = MeteoData.parse("Berlin/Germany=15.2");

        assertEquals(meteoData.getCity(), meteoData1.getCity());
        assertEquals(meteoData.getTemp(), meteoData1.getTemp());
        assertEquals(meteoData, meteoData1);
    }
}
