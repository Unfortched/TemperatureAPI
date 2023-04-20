package ru.sk.test;

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
public class Service {

    @Autowired
    Dao dao;

    public boolean checkId(Entity entity){
        return dao.checkId(entity.getId());
    }

    public String updateCurrent(Entity entity){
        dao.modify(entity);
        return dao.getDbJson(entity.getId());
    }

}
