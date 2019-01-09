package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controler odpowiadajacy za wyświetlanie strony profilu lekarza.
 */
@Controller
public class DoctorProfileController {

    /**
     * Metoda zwracająca nazwę widoku strony profilu lekarza
     *
     * @param userId - id lekarza
     * @param model  - model widoku
     * @return "doctorProfile" - nazwa widoku strony.
     */
    @GetMapping("/{userId}/doctorProfile")
    public String getDoctorProfile(@PathVariable("userId") String userId, Model model) {
        model.addAttribute("id", userId);
        return "doctorProfile";
    }
}
