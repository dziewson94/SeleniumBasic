package com.sii.sup.base;

public enum Attributes {
    ID("id"),
    VALUE("value"),
    INPUT("input"),
    NAME("name"),
    SELECT("select");

    private final String value;

    private Attributes(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
