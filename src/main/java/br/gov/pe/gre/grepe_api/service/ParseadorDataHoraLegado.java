package br.gov.pe.gre.grepe_api.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interpreta data/hora gravadas pelo GREPE legado (ex.: "21 / 05 / 2026", "00 / 00 / 0000").
 */
public final class ParseadorDataHoraLegado {

    private static final Pattern DATA_COM_BARRAS = Pattern.compile("(\\d{1,2})\\s*/\\s*(\\d{1,2})\\s*/\\s*(\\d{2,4})");

    private static final DateTimeFormatter[] FORMATOS_DATA = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    private static final DateTimeFormatter[] FORMATOS_HORA = {
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HHmmss"),
            DateTimeFormatter.ofPattern("HHmm")
    };

    private ParseadorDataHoraLegado() {
    }

public static LocalDate parseData(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        // Remove apenas os espaços das pontas
        String valor = data.trim();

        // 1. Tenta achar o padrão legado com barras e espaços em qualquer parte da string
        Matcher matcher = DATA_COM_BARRAS.matcher(valor);
        if (matcher.find()) { // Mudamos de matches() para find()!
            int dia = Integer.parseInt(matcher.group(1));
            int mes = Integer.parseInt(matcher.group(2));
            int ano = normalizarAno(Integer.parseInt(matcher.group(3)));
            if (dia == 0 || mes == 0 || ano == 0) {
                return null;
            }
            try {
                return LocalDate.of(ano, mes, dia);
            } catch (RuntimeException e) {
                return null;
            }
        }

        // 2. Se não achou, remove todos os espaços para tentar os formatos padrão
        String compacto = valor.replace(" ", "");
        
        // Aplica a tesoura de segurança APENAS na versão sem espaços
        // Isso previne que horas embutidas (ex: 07/02/202400:00) quebrem o parser
        if (compacto.length() > 10) {
            compacto = compacto.substring(0, 10);
        }

        if (compacto.contains("00/00/0000") || compacto.equals("00/00/00")) {
            return null;
        }

        // 3. Tenta passar nos formatadores normais do Java
        for (DateTimeFormatter formato : FORMATOS_DATA) {
            try {
                return LocalDate.parse(compacto, formato);
            } catch (DateTimeParseException ignored) {
            }
        }

        return null;
    }

    public static LocalTime parseHora(String hora) {
        if (hora == null || hora.isBlank()) {
            return null;
        }

        String valor = hora.trim();
        if (valor.equals("00:00") || valor.equals("00:00:00")) {
            return null;
        }

        if (valor.length() == 7 && valor.lastIndexOf(':') == 5) {
            valor = valor.substring(0, 5);
        }

        for (DateTimeFormatter formato : FORMATOS_HORA) {
            try {
                return LocalTime.parse(valor, formato);
            } catch (DateTimeParseException ignored) {
            }
        }

        String soDigitos = valor.replaceAll("[^0-9]", "");
        if (soDigitos.length() == 4) {
            try {
                return LocalTime.of(
                        Integer.parseInt(soDigitos.substring(0, 2)),
                        Integer.parseInt(soDigitos.substring(2, 4)));
            } catch (RuntimeException ignored) {
            }
        }
        if (soDigitos.length() == 6) {
            try {
                return LocalTime.of(
                        Integer.parseInt(soDigitos.substring(0, 2)),
                        Integer.parseInt(soDigitos.substring(2, 4)),
                        Integer.parseInt(soDigitos.substring(4, 6)));
            } catch (RuntimeException ignored) {
            }
        }

        return null;
    }

    public static String normalizarMatricula(String matricula) {
        if (matricula == null) {
            return "";
        }
        return matricula.trim();
    }

    public static boolean isDataPlaceholder(String data) {
        if (data == null || data.isBlank()) {
            return true;
        }
        String compacto = data.trim().replace(" ", "");
        return compacto.equals("00/00/0000") || compacto.equals("00/00/00");
    }

    private static int normalizarAno(int ano) {
        if (ano < 100) {
            return ano >= 50 ? 1900 + ano : 2000 + ano;
        }
        return ano;
    }
}
