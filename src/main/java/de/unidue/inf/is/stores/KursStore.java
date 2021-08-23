package de.unidue.inf.is.stores;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import de.unidue.inf.is.domain.Benutzer;
import de.unidue.inf.is.domain.Kurs;
import de.unidue.inf.is.utils.DBUtil;



public final class KursStore implements Closeable {

    private Connection connection;
    private boolean complete;
    private String schema="dbp213" ;

    public KursStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }


    public List<Kurs> getMkurse(Benutzer benutzer) throws StoreException {
        List<Kurs> meineKurse=new ArrayList<>();
        try {
            PreparedStatement pstm = connection
                            .prepareStatement("SELECT tmp.kid,tmp.name,tmp.beschreibungstext,tmp.einschreibeschluessel,tmp.freiePlaetze , b.name as erstellername\n" +
                                    "FROM \n" +
                                    "(SELECT *         \n" +
                                    "FROM "+schema+".kurs\n" +
                                    "WHERE kid \n" +
                                    "in (SELECT kid FROM "+schema+".einschreiben WHERE bnummer=?)) tmp \n" +
                                    "JOIN \n" +
                                    schema+".benutzer b ON tmp.ersteller=b.bnummer");

            pstm.setInt(1, benutzer.getBnummer());

            ResultSet rs=pstm.executeQuery();
            while (rs.next()){
                int kid=rs.getInt("kid");
                String name=rs.getString("name");
                String beschreibungstext=rs.getString("beschreibungstext");
                String einschreibeschluessel=rs.getString("einschreibeschluessel");
                int freiePlaetze=rs.getInt("freiePlaetze");
                String erstellername=rs.getString("erstellername");

                meineKurse.add(new Kurs(kid,name,beschreibungstext,einschreibeschluessel,freiePlaetze,0,erstellername));


            }
            return meineKurse;
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    public List<Kurs> getVkurse(Benutzer benutzer) throws StoreException {
        List<Kurs> vKurse=new ArrayList<>();
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("SELECT tmp.kid,tmp.name,tmp.beschreibungstext,tmp.einschreibeschluessel,tmp.freiePlaetze , b.name as erstellername\n" +
                            "FROM\n " +
                            "(SELECT * \n" +
                            "FROM "+schema+".Kurs\n" +
                            "WHERE freiePlaetze > 0 AND kid NOT IN (SELECT e.kid FROM "+schema+".einschreiben e WHERE e.bnummer=? ) ) tmp\n" +
                            "JOIN \n" +
                            schema+".benutzer b ON tmp.ersteller=b.bnummer");
            pstm.setInt(1,benutzer.getBnummer());

            ResultSet rs=pstm.executeQuery();
            while (rs.next()){
                int kid=rs.getInt("kid");
                String name=rs.getString("name");
                String beschreibungstext=rs.getString("beschreibungstext");
                String einschreibeschluessel=rs.getString("einschreibeschluessel");
                int freiePlaetze=rs.getInt("freiePlaetze");
                //int erstellerid=rs.getInt("erstellerid");
                String erstellername=rs.getString("erstellername");

                vKurse.add(new Kurs(kid,name,null,null,freiePlaetze,0,erstellername));
            }
            return vKurse;
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    // create a new course

    public void createNewKurs(Benutzer ersteller,String name ,String einschreibschlüssel ,int  freieplaetze,String beschreibnugstext) throws StoreException {
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("INSERT INTO "+schema+".kurs(\n" +
                            "name,\n" +
                            "beschreibungstext,\n" +
                            "einschreibeschluessel,\n" +
                            "freiePlaetze,\n" +
                            "ersteller)\n " +
                            "            VALUES(?,?,?,?,?)");
            pstm.setString(1,name);
            pstm.setString(2,beschreibnugstext);

            // checking on password if it leer then you have to add null !!
            if (einschreibschlüssel.equals("")){
                pstm.setString(3,null);
            }else{
                pstm.setString(3,einschreibschlüssel);
            }

            // the creator will be enrolled and is not calculated in the freieplaetze
            pstm.setInt(4,freieplaetze);
            pstm.setInt(5,ersteller.getBnummer());

            pstm.executeUpdate();
            complete();
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    public  void deleteCourse (int kid) throws StoreException, SQLException {
        try {
            // It is a long transaction because we didn't change the database and add (ON DELETE CASCADE)
            // but bewerten should be deleted alone anyway
            // the others can be deleted automatically just by deleting the kurs


            // to delete bewerten
            PreparedStatement delBewerten = connection
                    .prepareStatement("DELETE FROM "+schema+".bewerten \n" +
                            "\n" +
                            "WHERE aid in\n" +
                            "\n" +
                            "(SELECT delBewertung.aid\n" +
                            "FROM\n" +
                            "(SELECT e.kid,tmp1.aid\n" +
                            "FROM\n" +
                            "(SELECT b.note , b.bnummer, a.aid\n" +
                            "FROM "+schema+".bewerten b JOIN "+schema+".abgabe a ON a.aid=b.aid) tmp1\n" +
                            "JOIN\n " +
                            schema+".einreichen e ON e.aid=tmp1.aid) delBewertung\n" +
                            "\n" +
                            "WHERE delBewertung.kid=?)");

            delBewerten.setInt(1,kid);
            // to delete Abgabe
            PreparedStatement delAbgabe = connection
                    .prepareStatement("DELETE FROM "+schema+".abgabe \n" +
                            "\n" +
                            "WHERE aid in  \n" +
                            "\n" +
                            "(SELECT aid\n" +
                            "FROM \n" +
                            "(SELECT e.kid , a.aid\n" +
                            "FROM\n " +
                            schema+".einreichen e \n" +
                            "JOIN "+schema+".abgabe a ON e.aid=a.aid) tmp\n" +
                            "\n" +
                            "WHERE tmp.kid=?)");

                    delAbgabe.setInt(1,kid);
            // to delete einreichen
            PreparedStatement delEinreichen = connection
                    .prepareStatement("DELETE FROM "+schema+".einreichen WHERE kid=?");

                    delEinreichen.setInt(1,kid);

            // to delete Aufgaben
            PreparedStatement delAufgaben = connection
                    .prepareStatement("DELETE FROM "+schema+".aufgabe WHERE kid=?");

            delAufgaben.setInt(1,kid);
            // to delete einschreiben
            PreparedStatement delEinschreiben = connection
                    .prepareStatement("DELETE FROM "+schema+".einschreiben WHERE kid=?");

            delEinschreiben.setInt(1,kid);
            // to delete Kurs
            PreparedStatement delKurs = connection
                    .prepareStatement("DELETE FROM "+schema+".kurs WHERE kid=?");

            delKurs.setInt(1,kid);

            delBewerten.executeUpdate();
            delEinreichen.executeUpdate();
            delAbgabe.executeUpdate();
            delAufgaben.executeUpdate();
            delEinschreiben.executeUpdate();
            delKurs.executeUpdate();
            // the transaction is succeed
            complete();

        } catch (SQLException e) {
            // Die Transaktion wurde nicht erfolgreich ausgeführt und rollback wird in die close methode aufgerufen
            throw new StoreException(e);
        }
    }
    public Kurs getKursInformation(int kid) throws StoreException {
        Kurs kursinfo = new Kurs();
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("SELECT tmp.ersteller as erstellerid,tmp.kid as kursinfoid,tmp.name,tmp.beschreibungstext,tmp.einschreibeschluessel,tmp.freiePlaetze , b.name as erstellername\n " +
                            "FROM\n" +
                            "(SELECT * \n" +
                            "FROM "+schema+".Kurs\n" +
                            "WHERE kid=?) tmp\n" +
                            "JOIN \n "
                            +schema+".benutzer b ON tmp.ersteller=b.bnummer");
            pstm.setInt(1,kid);
            ResultSet rs =pstm.executeQuery();
            while(rs.next()){
                int kursinfoid=rs.getInt("kursinfoid");
                String name=rs.getString("name");
                String beschreibungstext=rs.getString("beschreibungstext");
                String einschreibeschluessel=rs.getString("einschreibeschluessel");
                int freiePlaetze=rs.getInt("freiePlaetze"); // real_pf is the real free available place number
                String erstellername=rs.getString("erstellername");
                int erstellerId=rs.getInt("erstellerid");

                kursinfo.setKid(kursinfoid);
                kursinfo.setErstellername(erstellername);
                kursinfo.setFreiePlaetze(freiePlaetze);
                kursinfo.setName(name);
                kursinfo.setBeschreibungstext(beschreibungstext);
                kursinfo.setFreiePlaetze(freiePlaetze);
                kursinfo.setEinschreibeschluessel(einschreibeschluessel);
                kursinfo.setErstellerid(erstellerId);
            }
            complete();
            return kursinfo;
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
