package ru.meteo.api.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.vertx.core.json.JsonObject;

public class WeatherapiApiTest {

    public static void main(String[] args) throws UnirestException {
        Unirest.setTimeouts(20000, 15000);
        HttpResponse<String> response = Unirest
                .get("http://api.weatherapi.com/v1/current.json?key=15ab55e0872947fe957192259221309&q=Paris&aqi=no")
                .asString();

        JsonObject jsonObject = new JsonObject(response.getBody());
        System.out.println(jsonObject.encodePrettily());
        System.out.println(jsonObject.getJsonObject("current").getDouble("temp_c"));
    }
}
