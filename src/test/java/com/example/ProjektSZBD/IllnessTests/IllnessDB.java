package com.example.ProjektSZBD.IllnessTests;

import com.example.ProjektSZBD.Data.Illness;

import java.util.ArrayList;
import java.util.List;

class IllnessDB {

    List<Illness> getAllIllnesses() {
        ArrayList<Illness> illnesses = new ArrayList<>();
        illnesses.add(new Illness(1, "Choroba", "Opis"));
        illnesses.add(new Illness(2, "Choroba1", "Opis1"));
        return illnesses;
    }

    Illness getIllnessById(long id) {
        return new Illness(1, "Choroba", "Opis");
    }

    List<Illness> getAllIllnessesWithPattern(String pattern) {
        ArrayList<Illness> illnesses = new ArrayList<>();
        illnesses.add(new Illness(1, pattern + "1", "opis"));
        illnesses.add(new Illness(2, pattern + "2", "opis"));
        return illnesses;
    }
}
