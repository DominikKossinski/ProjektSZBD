package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony informacji o szpitalach.
 */
@Controller
public class HospitalsController {


    /**
     * Metoda odpowiedzialna zwracanie widoku strony głównej.
     *
     * @param model - model widoku
     * @return "Hospitals" - nazwa widoku strony głównej
     */
    @GetMapping("/hospitals")
    public String getHospitals(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        return "Hospitals";
    }
}
