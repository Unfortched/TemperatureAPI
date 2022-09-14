package ru.meteo.api.test;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RapidApiTest {
    public static void main(String[] args) throws UnirestException {
        Unirest.setTimeouts(20000, 15000);
        HttpResponse<String> response = Unirest
                .get("https://open-weather13.p.rapidapi.com/city/Paris")
                .header("X-RapidAPI-Key", "364cfaa2e8mshaa5d7a36eac6bb9p1e98ccjsn112732aa1351")
                .header("X-RapidAPI-Host", "open-weather13.p.rapidapi.com")
                .asString();

        JsonObject jsonObject = new JsonObject(response.getBody());
        System.out.println(jsonObject.getJsonObject("data").getDouble("temp_c"));
    }
}
