package ru.sk.test;

import io.vertx.core.dns.DnsResponseCode;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getInfo(@RequestBody Entity entity) {

        if(service.checkId(entity)) {
            return new ResponseEntity(new JsonObject(service.updateCurrent(entity)).encodePrettily(), HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
    }
}
