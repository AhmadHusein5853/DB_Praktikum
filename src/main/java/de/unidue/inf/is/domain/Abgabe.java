package de.unidue.inf.is.domain;

public class Abgabe {
    private int aid;
    private String abgabe_text;
    private String note;

    public Abgabe( int aid, String abgabe_text,String note) {
        this.aid = aid;
        this.abgabe_text = abgabe_text;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
