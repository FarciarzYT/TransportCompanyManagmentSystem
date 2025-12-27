package com.kiedywakacje.transport.models;

public enum CountryCodes {
    PL("Poland", "Polska"),
    DE("Germany", "Niemcy"),
    FR("France", "Francja"),
    ES("Spain", "Hiszpania"),
    UK("United Kingdom", "Wielka Brytania"),
    IT("Italy", "Włochy"),
    SE("Sweden", "Szwecja"),
    NL("Netherlands", "Holandia"),
    BE("Belgium", "Belgia"),
    CZ("Czech Republic", "Czechy"),
    SK("Slovakia", "Słowacja"),
    AT("Austria", "Austria"),
    DK("Denmark", "Dania"),
    NO("Norway", "Norwegia");

    private final String englishName;
    private final String polishName;

    CountryCodes(String englishName, String polishName) {
        this.englishName = englishName;
        this.polishName = polishName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getPolishName() {
        return polishName;
    }

    public static CountryCodes fromName(String name) {
        for (CountryCodes code : values()) {
            if (code.englishName.equalsIgnoreCase(name) || 
                code.polishName.equalsIgnoreCase(name) ||
                code.name().equalsIgnoreCase(name)) {
                return code;
            }
        }
        return null;
    }
}

