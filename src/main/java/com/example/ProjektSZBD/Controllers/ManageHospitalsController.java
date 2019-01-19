package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania szpitalami.
 */
@Controller
public class ManageHospitalsController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania szpitalami.
     *
     * @return "ManageHospitals" - nazwa widoku strony zarządzania szpitalami.
     */
    @GetMapping(value = "/admin/manageHospitals")
    public String manageHospitals() {
        return "ManageHospitals";
    }
}
