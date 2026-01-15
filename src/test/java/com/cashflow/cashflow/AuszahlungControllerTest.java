package com.cashflow.cashflow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuszahlungController.class)
@Import(JacksonTestConfig.class)
class AuszahlungControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuszahlungService auszahlungService;

    // 1️ GET-auszahlungen
    @Test
    @DisplayName("should return all auszahlungen")
    void should_return_all_auszahlungen() throws Exception {
        doReturn(List.of(
                new Auszahlung(
                        "Netflix",
                        BigDecimal.valueOf(15),
                        LocalDate.of(2024, 1, 1),
                        Auszahlung.Zahlungsart.KARTE,
                        Auszahlung.Verwendungszweck.FREIZEIT,
                        null
                )
        )).when(auszahlungService).getAllTransactions();

        mockMvc.perform(get("/auszahlungen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Netflix"));
    }

    // 2️ GET-auszahlungen-filter kategorie=MIETE
    @Test
    @DisplayName("should filter by category")
    void should_filter_by_category() throws Exception {
        doReturn(List.of()).when(auszahlungService)
                .getByKategorie(Auszahlung.Verwendungszweck.MIETE);

        mockMvc.perform(get("/auszahlungen/filter")
                        .param("kategorie", "MIETE"))
                .andExpect(status().isOk());
    }

    // 3️ POST-auszahlungen
    @Test
    @DisplayName("should create auszahlung")
    void should_create_auszahlung() throws Exception {
        doReturn(new Auszahlung(
                "Miete",
                BigDecimal.valueOf(500),
                LocalDate.of(2024, 1, 1),
                Auszahlung.Zahlungsart.UEBERWEISUNG,
                Auszahlung.Verwendungszweck.MIETE,
                null
        )).when(auszahlungService).addTransaction(any());

        mockMvc.perform(post("/auszahlungen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Miete",
                                  "betrag": 500,
                                  "datum": "2024-01-01",
                                  "zahlungsart": "UEBERWEISUNG",
                                  "verwendungszweck": "MIETE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Miete"));
    }

    // 4️ DELETE-auszahlungen/{id}
    @Test
    @DisplayName("should delete auszahlung")
    void should_delete_auszahlung() throws Exception {
        mockMvc.perform(delete("/auszahlungen/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    // 5️ GET-auszahlungen-filter datum=...
    @Test
    @DisplayName("should filter by date")
    void should_filter_by_date() throws Exception {
        doReturn(List.of()).when(auszahlungService)
                .getByDatum(LocalDate.of(2024, 1, 1));

        mockMvc.perform(get("/auszahlungen/filter")
                        .param("datum", "2024-01-01"))
                .andExpect(status().isOk());
    }

    // 6️ GET-auszahlungen-filter name=Net
    @Test
    @DisplayName("should filter by name")
    void should_filter_by_name() throws Exception {
        doReturn(List.of()).when(auszahlungService)
                .getByName("Net");

        mockMvc.perform(get("/auszahlungen/filter")
                        .param("name", "Net"))
                .andExpect(status().isOk());
    }

    // 7️ GET-auszahlungen-summe datum=...
    @Test
    @DisplayName("should return sum for a day")
    void should_return_sum_for_day() throws Exception {
        doReturn(BigDecimal.valueOf(42))
                .when(auszahlungService)
                .getSummeAmTag(LocalDate.of(2024, 1, 1));

        mockMvc.perform(get("/auszahlungen/summe")
                        .param("datum", "2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));
    }

    // 8️ GET-auszahlungen-summe-monat
    @Test
    @DisplayName("should return sum for month")
    void should_return_sum_for_month() throws Exception {
        doReturn(BigDecimal.valueOf(300))
                .when(auszahlungService)
                .getSummeProMonat(1, 2024);

        mockMvc.perform(get("/auszahlungen/summe-monat")
                        .param("monat", "1")
                        .param("jahr", "2024"))
                .andExpect(status().isOk())
                .andExpect(content().string("300"));
    }

    // 9️ GET-auszahlungen-chart
    @Test
    @DisplayName("should return chart data")
    void should_return_chart_data() throws Exception {
        doReturn(Map.of("MIETE", BigDecimal.valueOf(500)))
                .when(auszahlungService)
                .summeNachKategorie(1, 2024);

        mockMvc.perform(get("/auszahlungen/chart")
                        .param("monat", "1")
                        .param("jahr", "2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.MIETE").value(500));
    }
}
