package ru.meteo.api.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.vertx.core.json.JsonObject;

public class OpenWeatherTest {
    public static void main(String[] args) throws UnirestException {
        Unirest.setTimeouts(20000, 15000);
        HttpResponse<String> response = Unirest
                .get("https://api.openweathermap.org/data/2.5/weather?q=Paris&appid=cdcd124fe415e38eeacafe2c0ea29c71&units=metric")
                .asString();

        JsonObject jsonObject = new JsonObject(response.getBody());
        System.out.println(jsonObject.encodePrettily());
        System.out.println(jsonObject.getJsonObject("main").getDouble("temp"));
    }
}
