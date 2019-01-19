package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getHospitalId;
import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania lekarzami.
 */
@Controller
public class ManageDoctorsController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania lekarzami.
     *
     * @param model - model widoku
     * @return "manageDoctors" - nazwa widoku strony zarządzania lekarzami
     */
    @GetMapping("/manageDoctors")
    public String getAddDoctor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        model.addAttribute("id", authentication.getName());
        model.addAttribute("hospitalId", getHospitalId(Long.parseLong(authentication.getName())));
        return "ManageDoctors";
    }
}
