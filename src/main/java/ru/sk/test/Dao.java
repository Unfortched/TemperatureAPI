package ru.sk.test;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class Dao {

    @Autowired
    HikariDataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public boolean checkId(Integer id){

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource
                .addValue("id", id);

        Integer alreadyHaveRow = namedParameterJdbcTemplate.queryForObject("select count(*) from sk_example_table " +
                "where id = :id;", mapSqlParameterSource, Integer.class);

        return alreadyHaveRow > 0;
    }

    @Transactional()
    public void modify(Entity entity) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource
                .addValue("id", entity.getId())
                .addValue("add", entity.getAdd());

        namedParameterJdbcTemplate.update("update sk_example_table " +
                "set obj = jsonb_set(obj, '{current}', (COALESCE(obj->>'current','0')::int + :add)::text::jsonb) " +
                "where id = :id", mapSqlParameterSource);
    }

    @Transactional()
    public String getDbJson(Integer id){
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource
                .addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject("select obj from sk_example_table " +
                "where id = :id;", mapSqlParameterSource, String.class);
    }
}
