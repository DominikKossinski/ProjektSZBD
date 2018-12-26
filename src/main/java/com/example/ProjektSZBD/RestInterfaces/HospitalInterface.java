package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Hospital;

import java.util.List;

public interface HospitalInterface {

    List<Hospital> getAllHospitals();

    Hospital getHospitalById(int id);
}
