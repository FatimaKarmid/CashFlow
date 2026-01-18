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

import static org.mockito.ArgumentMatchers.*;
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

    //  GET /auszahlungen
    @Test
    @DisplayName("should return all auszahlungen")
    void should_return_all_auszahlungen() throws Exception {
        doReturn(List.of(new Auszahlung(
                "Netflix",
                BigDecimal.valueOf(15),
                LocalDate.of(2024, 1, 1),
                Auszahlung.Zahlungsart.KARTE,
                Auszahlung.Verwendungszweck.FREIZEIT,
                null
        ))).when(auszahlungService).getAllTransactions();

        mockMvc.perform(get("/auszahlungen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Netflix"));
    }

    //  GET /auszahlungen/filter?kategorie=MIETE
    @Test
    @DisplayName("should filter by category")
    void should_filter_by_category() throws Exception {
        doReturn(List.of()).when(auszahlungService)
                .filter(null, null, Auszahlung.Verwendungszweck.MIETE);

        mockMvc.perform(get("/auszahlungen/filter")
                        .param("kategorie", "MIETE"))
                .andExpect(status().isOk());
    }

    // GET /auszahlungen/filter?datum=2024-01-01
    @Test
    @DisplayName("should filter by date")
    void should_filter_by_date() throws Exception {
        doReturn(List.of()).when(auszahlungService)
                .filter(null, LocalDate.of(2024, 1, 1), null);

        mockMvc.perform(get("/auszahlungen/filter")
                        .param("datum", "2024-01-01"))
                .andExpect(status().isOk());
    }

    // GET /auszahlungen/filter?name=Net
    @Test
    @DisplayName("should filter by name")
    void should_filter_by_name() throws Exception {
        doReturn(List.of()).when(auszahlungService)
                .filter("Net", null, null);

        mockMvc.perform(get("/auszahlungen/filter")
                        .param("name", "Net"))
                .andExpect(status().isOk());
    }

    // GET /auszahlungen/filter?name=Net&kategorie=FREIZEIT&datum=2024-01-01
    @Test
    @DisplayName("should filter by name, category and date")
    void should_filter_by_all_params() throws Exception {
        doReturn(List.of()).when(auszahlungService)
                .filter("Net", LocalDate.of(2024, 1, 1), Auszahlung.Verwendungszweck.FREIZEIT);

        mockMvc.perform(get("/auszahlungen/filter")
                        .param("name", "Net")
                        .param("datum", "2024-01-01")
                        .param("kategorie", "FREIZEIT"))
                .andExpect(status().isOk());
    }

    // POST /auszahlungen
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

    // DELETE /auszahlungen/{id}
    @Test
    @DisplayName("should delete auszahlung")
    void should_delete_auszahlung() throws Exception {
        mockMvc.perform(delete("/auszahlungen/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    // GET /auszahlungen/summe
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

    // GET /auszahlungen/chart
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
