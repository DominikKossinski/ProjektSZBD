package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getHospitalId;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania oddziałami.
 */
@Controller
public class ManageHospitalSectionsController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania oddziałami.
     *
     * @param model - model widoku
     * @return "addIllness" - nazwa widoku strony zarządzania oddziałami
     */
    @GetMapping("/manageHospitalSections")
    public String getManageHospitalSections(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("id", authentication.getName());
        model.addAttribute("hospitalId", getHospitalId(Long.parseLong(authentication.getName())));
        return "ManageHospitalSections";
    }
}
