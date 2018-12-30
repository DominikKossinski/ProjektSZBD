package com.example.ProjektSZBD.HospitalTests;

import com.example.ProjektSZBD.Data.Doctors.Director;
import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.Hospital;

import java.util.ArrayList;
import java.util.List;

public class HospitalDB {

    public Hospital getHospitalById(int id) {
        return new Hospital(id, "Szpital", "ul. Rolna", "Poznań");
    }

    public Director getHospitalDirector(int hospitalId) {
        return new Director(hospitalId, 1, "Jan", "Nowak", 1);
    }

    public List<Director> getHospitalDirectors() {
        ArrayList<Director> directors = new ArrayList<>();
        directors.add(new Director(1, 1, "Jan", "Nowak", 1));
        directors.add(new Director(2, 2, "Jan", "Kowalski", 2));
        return directors;
    }

    public List<Ordynator> getHospitalOrdynators(int hospitalId) {
        ArrayList<Ordynator> ordynators = new ArrayList<>();
        ordynators.add(new Ordynator("Kardiologia", hospitalId, 1,
                "Jan", "Nowak", 1));
        ordynators.add(new Ordynator("Chirurgia", hospitalId, 2,
                "Jan", "Kowalski", 2));
        return ordynators;
    }

    public List<Hospital> getAllHospitals() {
        ArrayList<Hospital> hospitals = new ArrayList<>();
        hospitals.add(new Hospital(1, "Szpital", "ul. Rolna", "Poznań"));
        hospitals.add(new Hospital(2, "Szpital1", "ul. Rolna 1", "Poznań"));
        return hospitals;
    }
}
