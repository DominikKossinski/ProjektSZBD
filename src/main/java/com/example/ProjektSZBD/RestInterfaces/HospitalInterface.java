package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Hospital;

import java.util.List;

/**
 * Interfejs odpowiadający za wystawianie danych o szpitalach.
 */
public interface HospitalInterface {

    /**
     * Metoda zwracająca listę wszystkich szpitali.
     *
     * @return (List of Hospital) - listę wszystkich szpitali
     */
    List<Hospital> getAllHospitals();

    /**
     * Metoda zwracająca szpital o żądanym id.
     * @param id - id szpitala
     * @return (Hospital) - szpital o żądanym id
     */
    Hospital getHospitalById(int id);
}
