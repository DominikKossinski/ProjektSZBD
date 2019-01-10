package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        ArrayList<String> list = new ArrayList<>(roles);
        if (list.get(0).compareTo("ROLE_Dyrektor") == 0) {
            model.addAttribute("isDirector", true);
        } else {
            model.addAttribute("isDirector", false);
        }
        return "doctorProfile";
    }
}
