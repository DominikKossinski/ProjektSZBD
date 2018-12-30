package com.example.ProjektSZBD.HospitalSectionTests;

import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.HospitalSection;

import java.util.ArrayList;
import java.util.List;

public class HospitalSectionDB {

    public List<HospitalSection> getHospitalSectionByHospitalId(int id) {
        ArrayList<HospitalSection> hospitalSections = new ArrayList<>();
        hospitalSections.add(new HospitalSection(1, "Kardiologia", id));
        hospitalSections.add(new HospitalSection(2, "Chirurgia", id));
        return hospitalSections;
    }

    public Ordynator getHospitalSectionOrdynator(int id) {
        return new Ordynator("Kardiologia", 1, 1,
                "Jan", "Nowak", 1);
    }
}
