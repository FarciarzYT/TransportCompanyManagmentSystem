package com.kiedywakacje.transport.models;


public enum PaymentType {
    CREDIT_CARD("Karta kredytowa"),
    DEBIT_CARD("Karta debetowa"),
    BLIK_NUMBER("BLIK"),
    CASH("Gotówka");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

