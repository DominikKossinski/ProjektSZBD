package com.example.ProjektSZBD.Controllers;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.RestControllers.DoctorRestController;
import com.example.ProjektSZBD.RestInterfaces.DoctorInterface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania pobytami.
 */
@Controller
public class ManageStaysController {

    /**
     * Metoda zwracająca nazwę widoku strony zarządzania pobytami.
     *
     * @param id    - id lekarza
     * @param model - model widoku
     * @return "ManageStays" - nazwa widoku strony.
     */
    @GetMapping("/{id}/manageStays")
    public String addStay(@PathVariable("id") long id,
                          @RequestParam(value = "pesel", defaultValue = "", required = false) String pesel,
                          Model model) {
        model.addAttribute("id", id);
        model.addAttribute("pesel", pesel);
        DoctorRestController doctorRestController = new DoctorRestController();
        DoctorInterface doctorInterface = doctorRestController.getDoctorInterface();
        Doctor doctor = doctorInterface.getDoctorsById(id);
        model.addAttribute("hospitalSectionId", doctor.getHospitalSectionId());
        return "ManageStays";
    }
}
