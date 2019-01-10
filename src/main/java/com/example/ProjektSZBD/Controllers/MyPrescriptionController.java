package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony wyświetlania recept pacjenta.
 */
@Controller
public class MyPrescriptionController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony wyświetlania pobytów pacjenta.
     *
     * @param model - model widoku
     * @return "addIllness" - nazwa widoku strony wyświetlania pobytów pacjenta
     */
    @GetMapping("/myPrescriptions")
    public String getMyPrescriptions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("pesel", authentication.getName());
        return "MyPrescriptions";
    }
}
