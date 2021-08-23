package de.unidue.inf.is.domain;

public class Kurs {
    private int kid;
    private String name;
    private String beschreibungstext;
    private String einschreibeschluessel;
    private int freiePlaetze;
    private int erstellerid;
    private String erstellername;

    public Kurs(){
    }

    public Kurs(int kid, String name, String beschreibungstext, String einschreibeschluessel, int freiePlaetze, int erstellerid,String erstellername) {
        this.kid = kid;
        this.name = name;
        this.beschreibungstext = beschreibungstext;
        this.einschreibeschluessel = einschreibeschluessel;
        this.freiePlaetze = freiePlaetze;
        this.erstellerid = erstellerid;
        this.erstellername=erstellername;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibungstext() {
        return beschreibungstext;
    }

    public void setBeschreibungstext(String beschreibungstext) {
        this.beschreibungstext = beschreibungstext;
    }

    public String getEinschreibeschluessel() {
        return einschreibeschluessel;
    }

    public void setEinschreibeschluessel(String einschreibeschluessel) {
        this.einschreibeschluessel = einschreibeschluessel;
    }

    public int getFreiePlaetze() {
        return freiePlaetze;
    }

    public void setFreiePlaetze(int freiePlaetze) {
        this.freiePlaetze = freiePlaetze;
    }


    public int getErstellerid() {
        return erstellerid;
    }

    public void setErstellerid(int erstellerid) {
        this.erstellerid = erstellerid;
    }

    public String getErstellername() {
        return erstellername;
    }

    public void setErstellername(String erstellername) {
        this.erstellername = erstellername;
    }
}


