package com.example.ProjektSZBD.Controllers;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.RestControllers.DoctorRestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania elementami wyposażenia.
 */
@Controller
public class ManageElementsController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania elementami wyposażenia.
     *
     * @param model - model widoku
     * @return "ManageElements" - nazwa widoku strony zarządzania elementami wyposażenia
     */
    @GetMapping("/manageElements")
    public String getManageElements(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Doctor doctor = new DoctorRestController().getDoctorInterface()
                .getDoctorsById(Long.parseLong(authentication.getName()));
        model.addAttribute("id", doctor.getId());
        model.addAttribute("hospitalSectionId", doctor.getHospitalSectionId());
        getUserRoles(model, authentication);
        return "ManageElements";
    }
}
