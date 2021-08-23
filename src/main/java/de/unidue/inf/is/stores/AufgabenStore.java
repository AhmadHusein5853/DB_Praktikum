package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import de.unidue.inf.is.SignedInUser;
import de.unidue.inf.is.domain.Abgabe;
import de.unidue.inf.is.domain.Aufgaben;
import de.unidue.inf.is.utils.DBUtil;



public final class AufgabenStore implements Closeable {

    private Connection connection;
    private boolean complete;
    private String schema="dbp213" ;

    public AufgabenStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    // getting all Aufgaben for View_My_Course !!!!!!!!!!!!
    public List<Aufgaben> getAufgaben(int kid) throws StoreException {
        List<Aufgaben> meineAufgaben=new ArrayList<>();

        try {
            PreparedStatement pstm = connection
                    .prepareStatement("SELECT * FROM "+schema+".aufgabe WHERE kid = ? ORDER BY anummer");

            pstm.setInt(1, kid);

            ResultSet rs=pstm.executeQuery();
            while (rs.next()){
                int kid2 = rs.getInt("kid");
                int anummer= rs.getInt("anummer");
                String name= rs.getString("name");
                String beschreibung = rs.getString("beschreibung");
                Aufgaben a = new Aufgaben(kid2,anummer,name,beschreibung);

                // we getting Abgabe for each Aufgabe
                Abgabe ab = getAbgabe(a.getKid(), SignedInUser.logedin.getBnummer(), a.getAnummer());
                a.setAbgabe(ab);
                meineAufgaben.add(a);
            }
            return meineAufgaben;
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    // getting all Aufgabe for New_Assignment !!!!!!!!!!!!
    public Aufgaben getAufgabe(int kid,int aufgabeNummer) throws StoreException {
        Aufgaben aufgabe=new Aufgaben();

        try {
            PreparedStatement pstm = connection
                    .prepareStatement("SELECT * FROM "+schema+".aufgabe WHERE kid = ? AND anummer= ?");

            pstm.setInt(1, kid);
            pstm.setInt(2,aufgabeNummer);

            ResultSet rs=pstm.executeQuery();
            while (rs.next()){
                int kid2 = rs.getInt("kid");
                int anummer= rs.getInt("anummer");
                String name= rs.getString("name");
                String beschreibung = rs.getString("beschreibung");
                aufgabe.setAnummer(anummer);
                aufgabe.setBeschreibung(beschreibung);
                aufgabe.setName(name);
                aufgabe.setKid(kid2);
            }
            return aufgabe;
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    public Abgabe getAbgabe(int kid ,int bnummer ,int anummer) throws StoreException {
        // if there is no abgabe for this Aufgabe !!
        Abgabe ab = new Abgabe (0,"Noch keine Abgabe" ,"");
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("\n" +
                            "SELECT tmp.aid as aid, tmp.kid as kid,tmp.name as name,tmp.bnummer as bnummer, ab.abgabetext as abgabetext \n" +
                            "FROM\n" +
                            "(SELECT e.kid , e.anummer , e.bnummer ,a.name  , e.aid\n" +
                            "FROM "+schema+".einreichen e \n" +
                            "JOIN "+schema+".aufgabe a ON a.kid=e.kid AND a.anummer=e.anummer) tmp\n" +
                            "\n" +
                            "JOIN "+schema+".abgabe ab ON tmp.aid=ab.aid AND tmp.kid=? AND tmp.bnummer=? AND tmp.anummer=?  \n" );

            pstm.setInt(1, kid);
            pstm.setInt(2, bnummer);
            pstm.setInt(3, anummer);

            ResultSet rs=pstm.executeQuery();
            // if there is an abgabe then we try to see if there is a Bewertung for it
            if(rs.next()){
                int aid = rs.getInt("aid");
                String abgabetext = rs.getString("abgabetext");

                // getting the note of the Abgabe
                String note = getBewertung(kid, bnummer,aid);

                ab = new Abgabe(aid,abgabetext,note);

            }
            return ab;
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    public String getBewertung(int kid ,int bnummer,int aid) throws StoreException {

        String noteStr = "noch keine Bewertung";
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("SELECT sum(note), count(*),e.aid FROM "+schema+".einreichen e JOIN " +
                            schema+".bewerten" +
                            " b ON e.aid=b.aid where e.kid=? AND e.bnummer=? AND e.aid=? group by e.aid, e.bnummer, e.kid" );

            pstm.setInt(1, kid);
            pstm.setInt(2,bnummer);
            pstm.setInt(3,aid);

            // the rs is 1 line or 0 lines

            ResultSet rs=pstm.executeQuery();
            if(rs.next()){
                float summe = rs.getFloat(1);
                float anzahl = rs.getFloat(2);
                float note = summe/ anzahl;
                noteStr = String.valueOf(note); // "" + note;

            }
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
        return noteStr;
    }
    public Boolean tryToSubmit(int kid ,int bnummer ,int anummer,String abgabetext) throws StoreException {

        try {
            // testing if the user made an submission before
            PreparedStatement hasSubmitted = connection
                    .prepareStatement("SELECT *\n" +
                            "FROM \n" +
                            schema+".einreichen e \n" +
                            "JOIN \n" +
                            schema+".abgabe a ON e.aid=a.aid AND e.kid=? AND e.bnummer=? AND e.anummer=?");
            hasSubmitted.setInt(1,kid);
            hasSubmitted.setInt(2,bnummer);
            hasSubmitted.setInt(3,anummer);

            ResultSet rs=hasSubmitted.executeQuery();
            if(rs.next()){
                // he made submission before
                return false;
            }
            else{ // there is no submission --> make one
                PreparedStatement abgabe= connection.
                        prepareStatement("INSERT INTO "+schema+".abgabe(abgabetext) VALUES(?)");
                abgabe.setString(1,abgabetext);

                abgabe.executeUpdate();
                // adding the last added aid in the abgabe table to the table einreichen
                PreparedStatement einreichen= connection.
                        prepareStatement("INSERT INTO "+schema+".einreichen VALUES(?,?,?,(SELECT aid FROM "+schema+".abgabe ORDER BY  aid DESC FETCH FIRST 1 ROWS ONLY))");
                einreichen.setInt(1,bnummer);
                einreichen.setInt(2,kid);
                einreichen.setInt(3,anummer);

                einreichen.executeUpdate();
                // complete the transaction
                complete();

                return true;
            }
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }





    public void complete() {
        complete = true;
    }


    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                if (complete) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                throw new StoreException(e);
            }
            finally {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    throw new StoreException(e);
                }
            }
        }
    }

}
