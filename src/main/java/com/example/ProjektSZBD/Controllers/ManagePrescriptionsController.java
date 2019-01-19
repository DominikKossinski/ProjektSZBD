package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania receptami.
 */
@Controller
public class ManagePrescriptionsController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania receptami.
     *
     * @return "addHospital" - nazwa widoku strony zarządzania receptami
     */
    @GetMapping("/managePrescriptions")
    public String getManagePrescriptions(@RequestParam(value = "pesel", defaultValue = "", required = false) String pesel, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("id", authentication.getName());
        model.addAttribute("pesel", pesel);
        getUserRoles(model, authentication);
        return "ManagePrescriptions";
    }
}
