package com.example.ProjektSZBD.ElementTests;

import com.example.ProjektSZBD.Data.Element;

import java.util.ArrayList;
import java.util.List;

class ElementDB {

    Element getElementById(int id) {
        return new Element(id, "Skalpel", 5, 5.5f, 1);
    }

    List<Element> getAllElements() {
        ArrayList<Element> elements = new ArrayList<>();
        elements.add(new Element(1, "Skalpel", 5, 5.5f, 1));
        elements.add(new Element(2, "Skalpel", 5, 5.5f, 2));
        return elements;
    }

    List<Element> getElementsByHospitalSectionId(int hospitalSectionId) {
        ArrayList<Element> elements = new ArrayList<>();
        elements.add(new Element(1, "Skalpel", 5, 5.5f, hospitalSectionId));
        elements.add(new Element(2, "Skalpel", 5, 5.5f, hospitalSectionId));
        return elements;
    }
}
