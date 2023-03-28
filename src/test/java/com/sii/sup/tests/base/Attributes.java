package com.sii.sup.tests.base;

public enum Attributes {
    ID("id"),
    VALUE("value"),
    INPUT("input"),
    TBODY("tbody"),
    TABLE_ROW("tr"),
    TABLE_COLUMN("td"),
    TABLE_HEADER("th"),
    BODY("body"),
    TYPE("type"),
    H3("h3"),
    CLASS("class"),
    LI("li");

    private final String value;

    Attributes(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
