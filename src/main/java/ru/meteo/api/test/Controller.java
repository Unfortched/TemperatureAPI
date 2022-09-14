package ru.meteo.api.test;

import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Slf4j
public class Controller {

    @Autowired
    Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String getInfo(@RequestParam(name = "city") String city,
                          @RequestParam(name = "date", required = false)
                          @DateTimeFormat(pattern = "dd.MM.yyyy") Date date) {
        return service.getInfo(city, date).encodePrettily();//.replace("\n", "<br>");
    }
}
