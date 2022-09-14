package ru.meteo.api.test;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MeteoData {
    private String city;
    private double temp;

    @Override
    public String toString() {
        return city + "=" + temp;
    }

    public static MeteoData parse(String data) {
        String[] dataParts = data.split("=");
        return MeteoData
                .builder()
                .city(dataParts[0])
                .temp(Double.parseDouble(dataParts[1]))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeteoData meteoData = (MeteoData) o;
        return city.equals(meteoData.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city);
    }
}
