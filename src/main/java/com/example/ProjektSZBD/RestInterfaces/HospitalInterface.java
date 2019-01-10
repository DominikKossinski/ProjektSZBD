package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Doctors.Director;
import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.Data.HospitalSection;

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
    Hospital getHospitalById(long id);

    /**
     * Metoda zwracająca szpital, którego częścią jest odział o podanym id.
     *
     * @param hospitalSectionId - id oddziału
     * @return (Hospital) - żądany szpital
     */
    Hospital getHospitalByHospitalSectionId(long hospitalSectionId);

    /**
     * Metoda zwracająca informacje na temat dyrektora szpitala.
     *
     * @param hospitalId - id szpitala
     * @return (Director) - obiekt reprezentujący dyrektora szpitala
     */
    Director getHospitalDirector(long hospitalId);

    /**
     * Metoda zwracająca listę dyrektorów.
     *
     * @return (List of Director) - listę obiektów reprezentujących dyrektorów szpitali
     */
    List<Director> getHospitalsDirectors();

    /**
     * Metoda zwracająca listę ordynatorów w danym szpitalu.
     *
     * @param hospitalId - id szpitala
     * @return (List of Ordynator) - listę obiektów reprezentujących ordynatorów szpitali
     */
    List<Ordynator> getHospitalOrdynators(long hospitalId);

    /**
     * Metoda służąca do dodania nowego szpitala.
     *
     * @param hospital - obiekt reprezentujący szpital
     * @param hospitalSection - obiekt reprezentujący oddział, na którym pracuje dyrektor
     * @param doctor - obiekt reprezentujący dyrektora szpitala
     * @return (long) - id szpitala w przypadku pomyślnego zakończenia dodawania
     * * kod błędu w przypadku błędu
     */
    long insertHospital(Hospital hospital, HospitalSection hospitalSection, Doctor doctor);

    /**
     * Metoda służąca do usuinięcia szpitala o podanym id.
     *
     * @param id - id elementu wyposażenia
     * @return (int) - 0 w przypadku pomyślnego zakończenia usuwania
     * * kod błędu w przypadku błędu
     */
    int deleteHospital(long id);

    /**
     * Metoda służąca do aktualizacji danych szpitala.
     *
     * @param hospital - obiekt reprezentujący element wyposażenia
     * @return (int) - 0 w przypadku pomyślnego zakończenia aktualizowania
     * * kod błędu w przypadku błędu
     */
    int updateHospital(Hospital hospital);

}
