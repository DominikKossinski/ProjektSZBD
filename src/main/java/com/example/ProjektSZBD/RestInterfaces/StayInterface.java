package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Stay;

import java.util.List;

/**
 * Interfejs odpowiedzialny za wystawianie danych o pobytach.
 */
public interface StayInterface {

    /**
     * Metoda zwracająca informacje o pobycie wyszukiwanym po id.
     *
     * @param id - id pobytu
     * @return (Stay) - obiekt reprezentujący pobyt
     */
    Stay getStayById(long id);

    /**
     * Metoda zwracająca pobyty danego pacjenta.
     *
     * @param pesel - pesel pacjenta
     * @return (List of Stay) - listę obiektów reprezentujących pobyty
     */
    List<Stay> getStayByPesel(long pesel);

    /**
     * Metoda zwracająca pobyty zarejestrowanych przez lekarza.
     *
     * @param doctorId - id lekarza
     * @return (List of Stay) - listę obiektów reprezentujących pobyty
     */
    List<Stay> getStayByDoctor(long doctorId);

    /**
     * Metoda do obsługi wstawiania pobytu.
     *
     * @param stay - obiekt reprezentujący pobyt
     * @return (long) - id pobytu w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    long insertStay(Stay stay);

    /**
     * Metoda do obsługi aktualizowania danych pobytu.
     *
     * @param stay - obiekt reprezentujący pobyt
     * @return (int) - 0 w przypadku pomyślnego zakończenia aktualizowania danych pobytu
     * kod błędu w przypadku błędu
     */
    int updateStay(Stay stay);

    /**
     * Metoda do obsługi usuwania pobytu.
     *
     * @param id - id pobytu
     * @return (int) - 0 w przypadku pomyślnego zakończenia usuwania
     * kod błędu w przypadku błędu
     */
    int deleteStay(long id);


}
