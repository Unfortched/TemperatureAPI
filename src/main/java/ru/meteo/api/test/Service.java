package ru.meteo.api.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Slf4j
@EnableScheduling
public class Service {

    @Autowired
    MapDB mapDB;

    public Service(MapDB mapDB) {
        this.mapDB = mapDB;
    }

    public JsonObject getInfo(String city, Date date) {

        date = date == null ? new Date(0L) : date;

        List<String> cities = getCitiesNames();

        Map<Long, String> infoMap = getDataByTimestamp(city, date.getTime());

        boolean errorPresence = !cities.contains(city) &&
                cities.stream()
                        .map(p -> p.split("/")[0])
                        .noneMatch(x -> x.equalsIgnoreCase(city));

        if (errorPresence || infoMap == null) {
            JsonObject error = new JsonObject();
            error.put("code", "404");
            error.put("info", "Selected city not configured");
            return error;
        }


        JsonObject result = new JsonObject();
        result.put("city", city);
        JsonArray tempHistory = new JsonArray();
        infoMap.forEach((key, value) -> {

            MeteoData meteoData = MeteoData.parse(value);

            JsonObject tempStep = new JsonObject();
            tempStep.put("date", Instant.ofEpochMilli(key).atZone(ZoneId.systemDefault())
                    .toLocalDate().format(DateTimeFormatter.ISO_DATE));
            tempStep.put("temperature", meteoData.getTemp());

            tempHistory.add(tempStep);
        });

        result.put("info", tempHistory);

        return result;
    }

    @Scheduled(fixedDelayString = "${timerate}")
    public void collectData() {

        log.info("scheduled update");

        List<MeteoData> citiesInfo = getCurrentMeteodata();

        Unirest.setTimeouts(20000, 15000);

        for (MeteoData data : citiesInfo) {

            String city = data.getCity().split("/")[0];

            MeteoData updateData = MeteoData.builder()
                    .city(data.getCity())
                    .temp(getAverageTemperature(city))
                    .build();

            mapDB.insertRecord(System.currentTimeMillis() + citiesInfo.indexOf(data),
                    updateData);
        }
        mapDB.commit();
    }

    public Map<Long, String> getDataByTimestamp(String city, Long timestamp) {

        try {
            Long plusDay = 86400000L;

            if (timestamp > 0) {

                return mapDB.getCache().entrySet().stream()
                        .filter(entry -> entry.getKey() >= timestamp && entry.getKey() <= timestamp + plusDay
                                && (entry.getValue().startsWith(city) || entry.getValue().equalsIgnoreCase(city)))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            Map.Entry<Long, String> lastData = mapDB.getCache().entrySet().stream()
                    .filter(entry -> entry.getValue().contains(city))
                    .max(Map.Entry.comparingByKey()).get();
            return Collections.singletonMap(lastData.getKey(), lastData.getValue());

        } catch (Exception e) {
            //log.error("error", e);
            return null;
        }
    }

    private List<MeteoData> getCurrentMeteodata() {
        return mapDB.getCache().values()
                .stream()
                .map(MeteoData::parse)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getCitiesNames() {
        return mapDB.getCache()
                .values()
                .stream()
                .map(p -> p.split("=")[0])
                .distinct()
                .collect(Collectors.toList());
    }

    private double getAverageTemperature(String city) {
        double avg = 0.0;

        avg += fetchOpenWeatherApiData(city);
        avg += fetchRapidApiData(city);
        avg += fetchWeatherApiData(city);

        return avg / 3;
    }

    private double fetchRapidApiData(String city) {
        try {
            HttpResponse<String> response = Unirest
                    .get("https://open-weather13.p.rapidapi.com/city/" + city)
                    .header("X-RapidAPI-Key", "364cfaa2e8mshaa5d7a36eac6bb9p1e98ccjsn112732aa1351")
                    .header("X-RapidAPI-Host", "open-weather13.p.rapidapi.com")
                    .asString();

            JsonObject jsonObject = new JsonObject(response.getBody());
            return jsonObject.getJsonObject("data").getDouble("temp_c");
        } catch (Exception e) {
            // log.error("error", e);
            return 0;
        }
    }

    private double fetchOpenWeatherApiData(String city) {
        try {
            HttpResponse<String> response = Unirest
                    .get("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=cdcd124fe415e38eeacafe2c0ea29c71&units=metric")
                    .asString();

            JsonObject jsonObject = new JsonObject(response.getBody());
            return jsonObject.getJsonObject("main").getDouble("temp");
        } catch (Exception e) {
            // log.error("error", e);
            return 0;
        }
    }

    private double fetchWeatherApiData(String city) {
        try {
            HttpResponse<String> response = Unirest
                    .get("http://api.weatherapi.com/v1/current.json?key=15ab55e0872947fe957192259221309&q=" + city + "&aqi=no")
                    .asString();

            JsonObject jsonObject = new JsonObject(response.getBody());
            return jsonObject.getJsonObject("current").getDouble("temp_c");
        } catch (Exception e) {
            // log.error("error", e);
            return 0;
        }
    }

}
