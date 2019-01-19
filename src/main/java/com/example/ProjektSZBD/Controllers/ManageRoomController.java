package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getHospitalId;
import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania pokojami.
 */
@Controller
public class ManageRoomController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania pokojami.
     *
     * @param model - model widoku
     * @return "ManageRooms" - nazwa widoku strony zarządzania pokojami.
     */
    @GetMapping("/manageRooms")
    public String getManageRooms(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        model.addAttribute("hospitalId", getHospitalId(Long.parseLong(authentication.getName())));
        return "ManageRooms";
    }
}
