package br.gov.pe.gre.grepe_api.service;

public final class FormatadorHoras {

    private FormatadorHoras() {
    }

    public static String formatarDuracao(int minutos) {
        int abs = Math.abs(minutos);
        int horas = abs / 60;
        int mins = abs % 60;
        String texto = horas + "h" + (mins < 10 ? "0" : "") + mins + "min";
        return minutos < 0 ? "-" + texto : texto;
    }

    public static String formatarSaldo(int minutos) {
        if (minutos == 0) {
            return "0h00min";
        }
        return (minutos > 0 ? "+" : "") + formatarDuracao(minutos);
    }
}
