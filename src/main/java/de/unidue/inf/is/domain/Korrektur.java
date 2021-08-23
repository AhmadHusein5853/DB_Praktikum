package de.unidue.inf.is.domain;

public class Korrektur {
    private int kid;
    private int bnummer;
    private int anummer;
    private String name;
    private int aid;
    private String abgabe_text;
    private String beschreibung;

    public Korrektur() {
    }

    public Korrektur(int kid, int bnummer, int anummer, String name, int aid, String abgabe_text, String beschreibung) {
        this.kid = kid;
        this.bnummer = bnummer;
        this.anummer = anummer;
        this.name = name;
        this.aid = aid;
        this.abgabe_text = abgabe_text;
        this.beschreibung = beschreibung;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public int getBnummer() {
        return bnummer;
    }

    public void setBnummer(int bnummer) {
        this.bnummer = bnummer;
    }

    public int getAnummer() {
        return anummer;
    }

    public void setAnummer(int anummer) {
        this.anummer = anummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAbgabe_text() {
        return abgabe_text;
    }

    public void setAbgabe_text(String abgabe_text) {
        this.abgabe_text = abgabe_text;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        return "Korrektur{" +
                "kid=" + kid +
                ", bnummer=" + bnummer +
                ", anummer=" + anummer +
                ", name='" + name + '\'' +
                ", aid=" + aid +
                ", abgabe_text='" + abgabe_text + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                '}';
    }
}
