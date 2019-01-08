package com.example.ProjektSZBD;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.Data.Patient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getInMemoryUserDetailsManager;
import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

/**
 * Klasa odpowiadająca za konfigurację bezpieczeństwa serwera.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Metoda odpowiadająca za konfigurację dostępu do poszczególnych url serwera.
     *
     * @param http (HttpSecurity) - httpSecurity do skonfigurowania
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/home", "/api/login", "/css/all/*", "/js/all/*").permitAll()
                .antMatchers("/api/logout").authenticated()

                .antMatchers("/admin/*", "/api/admin/*").hasRole("ADMIN")
                .anyRequest().denyAll()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/home")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .and().logout().logoutSuccessUrl("/home");
        http.csrf().disable();
    }


    /**
     * Metoda wczytująca dane użytkowników z bazy danych przy starcie serwera.
     *
     * @return wczytany servis danych użytkowników.
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        List<Doctor> doctors = jdbcTemplate.query("SELECT * FROM LEKARZE", (rs, arg1) -> new Doctor(rs.getLong("id_lekarza"), rs.getString("imie"),
                rs.getString("nazwisko"), rs.getDouble("placa"),
                rs.getLong("id_oddzialu"), rs.getString("stanowisko"),
                rs.getString("haslo")));
        for (Doctor doctor : doctors) {
            UserDetails userDetails = User.withUsername(String.valueOf(doctor.getId()))
                    .password(doctor.getPassword()).roles(doctor.getPosition()).build();
            getInMemoryUserDetailsManager().createUser(userDetails);
        }
        List<Patient> patients = jdbcTemplate.query("SELECT * FROM PACJENCI",
                (rs, arg1) -> new Patient(rs.getLong("pesel"), rs.getString("imie"),
                        rs.getString("nazwisko"), rs.getString("haslo")));
        for (Patient patient : patients) {
            UserDetails userDetails = User.withUsername(String.valueOf(patient.getPesel()))
                    .password(patient.getPassword()).roles("PATIENT").build();
            getInMemoryUserDetailsManager().createUser(userDetails);
        }
        UserDetails userDetails = User.withUsername("Admin")
                .password("admin").roles("ADMIN").build();
        getInMemoryUserDetailsManager().createUser(userDetails);
        return ProjektSzbdApplication.getInMemoryUserDetailsManager();
    }
}
