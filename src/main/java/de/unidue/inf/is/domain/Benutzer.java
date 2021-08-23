package de.unidue.inf.is.domain;

public final class Benutzer {

    private int bnummer;
    private String email ;
    private String name;

    public Benutzer() {
    }

    public Benutzer(int bnummer, String email,String name) {
        this.bnummer = bnummer;
        this.email = email;
        this.name = name;

    }

    public int getBnummer() {
        return bnummer;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
}