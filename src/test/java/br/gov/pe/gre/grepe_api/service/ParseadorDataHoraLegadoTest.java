package br.gov.pe.gre.grepe_api.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ParseadorDataHoraLegadoTest {

    @Test
    void parseDataComEspacosEstiloLegado() {
        LocalDate data = ParseadorDataHoraLegado.parseData("21 / 05 / 2026");
        assertEquals(LocalDate.of(2026, 5, 21), data);
    }

    @Test
    void parseDataPlaceholderInvalido() {
        assertNull(ParseadorDataHoraLegado.parseData("00 / 00 / 0000"));
    }

    @Test
    void parseDataFormatoSemEspacos() {
        assertEquals(LocalDate.of(2026, 5, 21), ParseadorDataHoraLegado.parseData("21/05/2026"));
    }
}
