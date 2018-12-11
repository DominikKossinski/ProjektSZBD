package com.example.ProjektSZBD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class ProjektSzbdApplication {

    private static String DBUrl;
    private static String DBUser;
    private static String DBPassword;

    private static JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.exit(-1);
        } else {
            DBUrl = args[0];
            DBUser = args[1];
            DBPassword = args[2];
            System.out.println("DB: '" + args[0] + "' '" + args[1] + "' '" + args[2] + "'");
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl(DBUrl);
        dataSource.setUsername(DBUser);
        dataSource.setPassword(DBPassword);
        jdbcTemplate = new JdbcTemplate(dataSource);
        String c = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PRACOWNICY", String.class);
        System.out.println(c);
        SpringApplication.run(ProjektSzbdApplication.class, args);
    }
}
