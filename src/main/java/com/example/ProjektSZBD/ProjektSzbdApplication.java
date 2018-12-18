package com.example.ProjektSZBD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class ProjektSzbdApplication {

    private static JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl(System.getenv("dbUrl"));
        dataSource.setUsername(System.getenv("dbUserName"));
        dataSource.setPassword(System.getenv("dbPassword"));
        jdbcTemplate = new JdbcTemplate(dataSource);
        String c = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PRACOWNICY", String.class);
        System.out.println(c);
        SpringApplication.run(ProjektSzbdApplication.class, args);
    }
}
