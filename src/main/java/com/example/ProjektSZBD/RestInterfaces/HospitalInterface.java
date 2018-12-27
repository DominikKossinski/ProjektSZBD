package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Doctors.Director;
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

    /**
     * Metoda zwracająca informacje na temat dyrektora szpitala.
     *
     * @param hospitalId - id szpitala
     * @return (Director) - obiekt reprezentujący dyrektora szpitala
     */
    Director getHospitalDirector(int hospitalId);

    /**
     * Metoda zwracająca listę dyrektorów.
     *
     * @return (List of Director) - listę obiektów reprezentujących dyrektorów szpitali
     */
    List<Director> getHospitalsDirectors();
}
