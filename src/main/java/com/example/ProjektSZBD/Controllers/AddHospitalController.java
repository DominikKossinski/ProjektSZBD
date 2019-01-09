package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony dodawania szpitala.
 */
@Controller
public class AddHospitalController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony dodawania szpitala.
     *
     * @return "home" - nazwa widoku strony głównej
     */
    @GetMapping("/admin/addHospital")
    public String addHospital() {
        return "addHospital";
    }
}
