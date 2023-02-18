package ru.sk.test.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DB {

    @Bean
    HikariDataSource dataSource(C config) {
        log.info("init DataSource start {} {} {} {}", config.DB_DRIVER, config.DB_URL, config.DB_LOGIN, config.DB_PASSWORD);
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(config.DB_DRIVER);
        hikariDataSource.setJdbcUrl(config.DB_URL);
        hikariDataSource.setUsername(config.DB_LOGIN);
        hikariDataSource.setPassword(config.DB_PASSWORD);
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setAutoCommit(false);
        hikariDataSource.setIsolateInternalQueries(true);
        hikariDataSource.setConnectionTestQuery("select 1");
        log.info("init DataSource done {} {} {} {}", config.DB_DRIVER, config.DB_URL, config.DB_LOGIN, config.DB_PASSWORD);
        return hikariDataSource;
    }
}
