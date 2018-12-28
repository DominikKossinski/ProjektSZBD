package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.HospitalSection;

import java.util.List;

/**
 * Interfejs odpowiadający za wystawienie danych o oddziałach.
 */
public interface HospitalSectionInterface {

    /**
     * Metoda zwracająca listę oddziałów w szpitalu o podanym id.
     *
     * @param id - id szpitala
     * @return (List of HospitalSection) - listę oddziałów w szpitalu o podanym id.
     */
    List<HospitalSection> getHospitalSectionsByHospitalId(int id);

    /**
     * Metoda zwracająca ordynatora oddziału.
     *
     * @param hospitalSectionId - id oddziału
     * @return (Ordynator) - ordynatora oddziału.
     */
    Ordynator getHospitalSectionOrdynator(int hospitalSectionId);
}
