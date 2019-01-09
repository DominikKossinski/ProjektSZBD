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
    Element getElementById(long id);

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
    List<Element> getElementsByHospitalSectionId(long hospitalSectionId);

    /**
     * Metoda zwracająca informację o wszystkich elementach wyposażenia w wybranym szpitalu.
     *
     * @param hospitalId - id szpitala
     * @return (List of element) - lista obiektów zawierających informacje o elementach wyposażenia
     */
    List<Element> getElementsByHospitalId(long hospitalId);

    /**
     * Metoda służąca do dodania nowego elementu wyposażenia.
     *
     * @param element - obiekt reprezentujący element wyposażenia
     * @return (long) - 0 w przypadku pomyślnego zakończenia dodawania
     * * kod błędu w przypadku błędu
     */
    long insertElement(Element element);

    /**
     * Metoda służąca do usuinięcia elementu wyposażenia o podanym id.
     *
     * @param id - id elementu wyposażenia
     * @return (int) - 0 w przypadku pomyślnego zakończenia usuwania
     * * kod błędu w przypadku błędu
     */
    int deleteElement(long id);

    /**
     * Metoda służąca do aktualizacji danych elementu wyposażenia.
     *
     * @param element - obiekt reprezentujący element wyposażenia
     * @return (int) - 0 w przypadku pomyślnego zakończenia aktualizowania
     * * kod błędu w przypadku błędu
     */
    int updateElement(Element element);
}
