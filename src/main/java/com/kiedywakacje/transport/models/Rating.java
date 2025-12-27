package com.kiedywakacje.transport.models;

public enum Rating {
    STAR5(5, "Doskonały"),
    STAR4(4, "Bardzo dobry"),
    STAR3(3, "Dobry"),
    STAR2(2, "Przeciętny"),
    STAR1(1, "Słaby");

    private final int value;
    private final String description;

    Rating(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}

