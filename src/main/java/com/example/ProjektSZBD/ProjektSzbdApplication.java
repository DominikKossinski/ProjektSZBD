package com.example.ProjektSZBD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class ProjektSzbdApplication {

    private static JdbcTemplate jdbcTemplate = null;

    public static void main(String[] args) {
        SpringApplication.run(ProjektSzbdApplication.class, args);
    }

    public static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
            dataSource.setUrl(System.getenv("dbUrl"));
            dataSource.setUsername(System.getenv("dbUserName"));
            dataSource.setPassword(System.getenv("dbPassword"));
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }
}
