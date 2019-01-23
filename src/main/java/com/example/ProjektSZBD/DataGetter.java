package com.example.ProjektSZBD;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.RestControllers.DoctorRestController;
import com.example.ProjektSZBD.RestControllers.HospitalRestController;
import com.example.ProjektSZBD.RestInterfaces.DoctorInterface;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class DataGetter {

    public static long getHospitalId(long doctorId) {
        DoctorRestController doctorRestController = new DoctorRestController();
        DoctorInterface doctorInterface = doctorRestController.getDoctorInterface();
        Doctor doctor = doctorInterface.getDoctorsById(doctorId);
        HospitalRestController hospitalRestController = new HospitalRestController();
        HospitalInterface hospitalInterface = hospitalRestController.getHospitalInterface();
        Hospital hospital = hospitalInterface.getHospitalByHospitalSectionId(doctor.getHospitalSectionId());
        return hospital.getId();
    }

    public static void getUserRoles(Model model, Authentication authentication) {
        if (authentication.getName().compareTo("anonymousUser") == 0) {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("isPatient", false);
            model.addAttribute("isDoctor", false);
        } else {
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("userId", authentication.getName());
            Set<String> roles = authentication.getAuthorities().stream()
                    .map(r -> r.getAuthority()).collect(Collectors.toSet());
            ArrayList<String> list = new ArrayList<>(roles);
            model.addAttribute("role", list.get(0));
            if (list.get(0).compareTo("ROLE_ADMIN") != 0 && list.get(0).compareTo("ROLE_PATIENT") != 0) {
                model.addAttribute("isDoctor", true);
                model.addAttribute("isPatient", false);
                if (list.get(0).compareTo("ROLE_Dyrektor") == 0) {
                    model.addAttribute("isDirector", true);
                } else if (list.get(0).compareTo("ROLE_Ordynator") == 0) {
                    model.addAttribute("isOrdynator", true);
                }
            } else if (list.get(0).compareTo("ROLE_PATIENT") == 0) {
                model.addAttribute("isPatient", true);
                model.addAttribute("isDoctor", false);
            } else if (list.get(0).compareTo("ROLE_ADMIN") == 0) {
                model.addAttribute("isAdmin", true);
            } else {
                model.addAttribute("isPatient", false);
                model.addAttribute("isDoctor", false);
            }
        }
    }
}
