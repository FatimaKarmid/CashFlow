package com.cashflow.cashflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.time.LocalDate;

@RestController
public class AuszahlungController {

    private final AuszahlungRepository auszahlungRepository;


    public AuszahlungController(AuszahlungRepository auszahlungRepository) {
        this.auszahlungRepository = auszahlungRepository;
    }


    @GetMapping("/auszahlungen")
    public List<Auszahlung> getAuszahlungen() {
        return auszahlungRepository.findAll();
    }


    @GetMapping("/auszahlungen/{datum}")
    public List<Auszahlung> getAuszahlungenByDatum(@PathVariable LocalDate datum) {
        return auszahlungRepository.findByDatum(datum);
    }
}
