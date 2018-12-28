package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Element;

import java.util.List;

/**
 * Interfejs odpowiadający za wystawienie danych o elementach wyposażenia.
 */
public interface ElementInterface {

    /**
     * Metoda zwracająca informacje o elemencie wyposażenia wyszukiwanym po id.
     *
     * @param id - id elementu
     * @return (Element) - obiekt zawierający informacje o elemencie wyposażenia
     */
    Element getElementById(int id);

    /**
     * Metoda zwracająca informację o wszystkich elementach wyposażenia.
     *
     * @return (List of element) - lista obiektów zawierających informacje o elementach wyposażenia
     */
    List<Element> getAllElements();

    /**
     * Metoda zawierająca informację o wszystkich elementach wyposażenia na wybranym oddziale.
     *
     * @param hospitalSectionId - id oddziału
     * @return (List of element) - lista obiektów zawierających informacje o elementach wyposażenia
     */
    List<Element> getElementsByHospitalSectionId(int hospitalSectionId);

    /**
     * Metoda zwracająca informację o wszystkich elementach wyposażenia w wybranym szpitalu.
     *
     * @param hospitalId - id szpitala
     * @return (List of element) - lista obiektów zawierających informacje o elementach wyposażenia
     */
    List<Element> getElementsByHospitalId(int hospitalId);
}
