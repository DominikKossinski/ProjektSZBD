package com.example.ProjektSZBD;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {

    @MockBean
    public UserDetailsService userDetailsService;


    @Bean
    public DataSource dataSource() {
        return Mockito.mock(DataSource.class);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Mockito.mock(EntityManagerFactory.class);
    }
}
