package com.cashflow.cashflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuszahlungService {

    @Autowired
    private final AuszahlungRepository repo;

    public AuszahlungService(AuszahlungRepository repo) {
        this.repo = repo;
    }

    public Auszahlung speichern(Auszahlung a) {
        return repo.save(a);
    }

    public List<Auszahlung> alle() {
        List<Auszahlung> auszahlungenList = new ArrayList<>();
        repo.findAll().forEach(auszahlungenList::add);
        return auszahlungenList;
    }

    public List<Auszahlung> amTag(LocalDate tag) {
        return repo.findByDatum(tag);
    }
}
