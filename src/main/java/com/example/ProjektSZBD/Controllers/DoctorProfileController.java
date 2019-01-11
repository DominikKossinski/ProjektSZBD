package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        return "doctorProfile";
    }
}
