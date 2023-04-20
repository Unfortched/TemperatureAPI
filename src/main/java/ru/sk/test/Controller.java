package ru.sk.test;

import io.vertx.core.dns.DnsResponseCode;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

        if(service.checkId(entity) && entity.getAdd() != null && entity.getId() != null) {
            return new ResponseEntity(new JsonObject(service.updateCurrent(entity)).encodePrettily(), HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
    }

    @RequestMapping(value = "/test" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> check(){
        JsonArray jsonArray = new JsonArray();

        for(int i = 0; i < 2; ++i){
            JsonObject jsonObject = new JsonObject("{\n" +
                    "\"signableId\": \"string\",\n" +
                    "\"name\": \"string\",\n" +
                    "\"signedXml\": \"string\"\n" +
                    "}");
            jsonArray.add(jsonObject);
        }
        return ResponseEntity.ok(jsonArray.encodePrettily());
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> test(){
        return ResponseEntity.ok(new JsonObject("{\n" +
                "\"access_token\": \"string\",\n" +
                "\"expires_in\": 0,\n" +
                "\"id_token\": \"string\",\n" +
                "\"scope\": \"string\",\n" +
                "\"token_type\": \"string\"\n" +
                "}").encodePrettily());
    }

    @RequestMapping(value = "/test3" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> check2(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("error", "123");
        jsonObject.put("errorDescription", "123432432");
        return ResponseEntity.ok(jsonObject.encodePrettily());
    }
}
