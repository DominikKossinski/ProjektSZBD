package com.example.ProjektSZBD;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.Data.Patient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                .antMatchers("/", "/home", "/api/login", "/css/all/*", "/js/all/*", "/img/*", "/api/hospitalSection**",
                        "/illnesses", "/api/illness**", "/api/allHospitals", "/api/hospitalSections**", "/hospitals").permitAll()
                .antMatchers("/api/logout").authenticated()

                .antMatchers("/api/salary**").access("@webSecurityConfig.isDirectorOrAdmin(authentication)")

                .antMatchers("/css/director/*", "/js/director/*", "/manageDoctors",
                        "/api/hospitalSections**", "/manageHospitalSections",
                        "/api/updateHospitalSection", "/api/addHospitalSection", "/api/deleteHospitalSection",
                        "/api/rooms*", "/manageRooms", "/api/addRoom", "/api/updateRoom", "/api/deleteRoom",
                        "/api/doctors**", "/api/addDoctor", "/api/updateDoctor", "/api/deleteDoctor**").hasRole("Dyrektor")
                .antMatchers("/css/ordynator/*", "/js/ordynator/*", "/manageElements", "/api/elements**",
                        "/api/addElement", "/api/updateElement", "/api/deleteElement**").hasRole("Ordynator")
                /*.antMatchers("/css/doctor/*", "/js/doctor/*").hasRole("Lekarz")
                .antMatchers("/css/doctor/*", "/js/doctor/*").hasRole("Asystent")
                .antMatchers("/css/doctor/*", "/js/doctor/*").hasRole("Rezydent")
                .antMatchers("/css/doctor/*", "/js/doctor/*").hasRole("Praktykant")*/

                .antMatchers("/admin/*", "/api/admin/*", "/js/admin/*", "/css/admin/*",
                        "/adminPanel", "/api/addSalary**", "/api/updateSalary",
                        "/api/deleteSalary").hasRole("ADMIN")

                .antMatchers("/js/patient/*", "/css/patient/*", "/myStays", "/myPrescriptions").access(
                "@webSecurityConfig.isPatient(authentication)")
                .antMatchers("/api/rooms**", "/js/doctor/*", "/css/doctor/*", "/managePatients", "/api/addPatient",
                        "/api/deletePatient", "/api/updatePatient", "/api/searchPatients**",
                        "/api/addStay", "/api/addIllness", "/api/updateIllness", "/api/deleteIllness**",
                        "/manageIllnesses", "/api/prescriptions**", "/managePrescriptions", "/api/addPrescription", "/api/updatePrescription",
                        "/api/deletePrescription**", "/api/stays**").access("@webSecurityConfig.isDoctor(authentication)")

                .antMatchers("/api/patient/{pesel}/**", "/patient/{pesel}/**").access(
                "@webSecurityConfig.checkPatientPesel(authentication, #pesel)")
                .antMatchers("/api/{userId}/**", "/{userId}/**").access(
                "@webSecurityConfig.checkDoctorId(authentication, #userId)")
                // .antMatchers().access("@webSecurityConfig.checkDoctorId(authentication, #userId)")

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

    public boolean checkPatientPesel(Authentication authentication, String pesel) {
        String userName = authentication.getName();
        System.out.println("Check id name = " + userName + " id = " + pesel);
        return userName.compareTo(pesel) == 0 && isPatient(authentication);
    }

    public boolean checkDoctorId(Authentication authentication, String id) {
        String userName = authentication.getName();
        System.out.println("Check id name = " + userName + " id = " + id);
        return userName.compareTo(id) == 0 && isDoctor(authentication);
    }

    public boolean isDoctor(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        ArrayList<String> list = new ArrayList<>(roles);
        System.out.println("Role: " + list.get(0));
        return list.get(0).compareTo("ROLE_ADMIN") != 0 && list.get(0).compareTo("ROLE_PATIENT") != 0
                && list.get(0).compareTo("ROLE_ANONYMOUS") != 0;
    }

    public boolean isPatient(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        ArrayList<String> list = new ArrayList<>(roles);
        return list.get(0).compareTo("ROLE_PATIENT") == 0;
    }

    public boolean isDirectorOrAdmin(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        ArrayList<String> list = new ArrayList<>(roles);
        System.out.println("is Admin or director" +
                (list.get(0).compareTo("ROLE_ADMIN") == 0 || list.get(0).compareTo("ROLE_Dyrektor") == 0));
        return list.get(0).compareTo("ROLE_ADMIN") == 0 || list.get(0).compareTo("ROLE_Dyrektor") == 0;
    }
}
