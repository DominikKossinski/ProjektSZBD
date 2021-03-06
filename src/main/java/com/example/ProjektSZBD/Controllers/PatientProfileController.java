package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony profilu pacjenta.
 */
@Controller
public class PatientProfileController {

    /**
     * Metoda zwracająca nazwę widoku strony profilu pacjenta.
     *
     * @param id    - pesel
     * @param model - model widoku
     * @return "doctorProfile" - nazwa widoku strony.
     */
    @GetMapping("/patient/{id}/patientProfile")
    public String getPatientProfile(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        return "PatientProfile";
    }
}
