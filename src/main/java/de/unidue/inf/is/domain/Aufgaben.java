package de.unidue.inf.is.domain;

import de.unidue.inf.is.SignedInUser;
import de.unidue.inf.is.stores.AufgabenStore;

import java.io.IOException;

public class Aufgaben {

    private int kid;
    private int anummer;
    private String name;
    private String beschreibung;
    private Abgabe abgabe;

    public void setAbgabe(Abgabe abgabe) {
        this.abgabe = abgabe;
    }

    @Override
    public String toString() {
        return "Aufgaben{" +
                "kid=" + kid +
                ", anummer=" + anummer +
                ", name='" + name + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                '}';
    }

    public Aufgaben() {
    }

    public Aufgaben(int kid, int anummer, String name, String beschreibung) {
        this.kid = kid;
        this.anummer = anummer;
        this.name = name;
        this.beschreibung = beschreibung;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
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

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    //
    public Abgabe getAbgabe() throws IOException {

        return abgabe;
    }

 /*   public Float getBewertung() throws IOException {
        Float note;
        int aid;
        try (AufgabenStore aufgabendb = new AufgabenStore()) {

            aid = aufgabendb.getAid(kid, SignedInUser.logedin.getBnummer(), anummer);

            if (aid != 0) { // here It can a Bewertung exist or not
                note = aufgabendb.getBewertung(kid, SignedInUser.logedin.getBnummer(), aid);
                return note;


            }
            return -1.0f;


        }
    }*/
}


