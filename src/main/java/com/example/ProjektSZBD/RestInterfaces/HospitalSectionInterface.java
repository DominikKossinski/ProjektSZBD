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
    List<HospitalSection> getHospitalSectionsByHospitalId(long id);

    /**
     * Metoda zwracająca oddział o podanym id.
     *
     * @param id - id oddziału
     * @return (HospitalSection) - oddział o podanym id.
     */
    HospitalSection getHospitalSectionById(long id);

    /**
     * Metoda zwracająca ordynatora oddziału.
     *
     * @param hospitalSectionId - id oddziału
     * @return (Ordynator) - ordynatora oddziału.
     */
    Ordynator getHospitalSectionOrdynator(long hospitalSectionId);

    /**
     * Metoda do obsługi wstawiania oddziału.
     *
     * @param hospitalSection - obiekt reprezentujący oddział
     * @return (long) - id oddziału w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    long insertHospitalSection(HospitalSection hospitalSection);

    /**
     * Metoda do obsługi aktualizacji danych oddziału
     *
     * @param hospitalSection - obiekt reprezentujący oddział
     * @return (long) - 0 w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    int updateHospitalSection(HospitalSection hospitalSection);

    /**
     * Metoda do obsługi usuwania oddziału.
     *
     * @param id - id oddziału
     * @return (long) - 0 w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    int deleteHospitalSection(long id);
}
