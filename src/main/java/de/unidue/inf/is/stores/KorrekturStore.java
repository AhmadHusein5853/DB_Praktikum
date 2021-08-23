package de.unidue.inf.is.stores;
import de.unidue.inf.is.domain.Korrektur;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KorrekturStore  implements Closeable {
    private Connection connection;
    private boolean complete;
    private String schema="dbp213" ;

    public KorrekturStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }


    public ArrayList<Korrektur> getAlleAbgaben(int kid, int bnummer) throws SQLException {
        ArrayList<Korrektur> result =new ArrayList<>();

        try {
            PreparedStatement pstm = connection.
                    prepareStatement("\n" +
                            "SELECT tmp.kid as kid , tmp.bnummer as bnummer ,tmp.anummer as anummer, tmp.name as name, tmp.aid as aid, ab.abgabetext as abgabetext ,tmp.beschreibung as beschreibung\n" +
                            "FROM \n" +
                            "(SELECT e.kid , e.bnummer ,e.anummer , a.name , e.aid , a.beschreibung \n" +
                            "\n" +
                            "FROM \n" +
                            schema+".einreichen e JOIN "+schema+".aufgabe a ON e.kid=a.kid AND a.anummer = e.anummer) tmp \n" +
                            "\n" +
                            "JOIN "+schema+".abgabe ab ON tmp.aid=ab.aid AND tmp.kid=? AND bnummer <> ?");

            pstm.setInt(1,kid);
            pstm.setInt(2,bnummer);
            ResultSet rs=pstm.executeQuery();

            while(rs.next()){
                int bnummer1 = rs.getInt("bnummer");
                int anummer = rs.getInt("anummer");
                String name = rs.getString("name");
                int aid = rs.getInt("aid");
                String abgabeText = rs.getString("abgabetext");
                String beschreibung = rs.getString("beschreibung");
                result.add(new Korrektur(kid,bnummer1,anummer,name,aid,abgabeText,beschreibung));

            } }catch (SQLException throwables) {
            throwables.printStackTrace();
       }
            return result;
        }

        // was den Benutzer schon bewertet hat
    public ArrayList<Korrektur> getMeineKorrekture(int bnummer1) throws SQLException {
        ArrayList<Korrektur> result =new ArrayList<>();

        try {
            PreparedStatement pstm = connection.
                    prepareStatement("SELECT bnummer , aid FROM "+schema+".bewerten WHERE bnummer = ?");

            pstm.setInt(1,bnummer1);
            ResultSet rs=pstm.executeQuery();

            while(rs.next()){
                Korrektur korrek = new Korrektur();

                int bnummer = rs.getInt("bnummer");
                int aid = rs.getInt("aid");
                korrek.setBnummer(bnummer);
                korrek.setAid(aid);
                result.add(korrek);


            } }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }


    public void insertRating(int bnummer,int aid , int note,String kommentar) throws StoreException {
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("INSERT INTO "+schema+".bewerten (bnummer,aid,note,kommentar) VALUES (?,?,?,?)");
            pstm.setInt(1,bnummer);
            pstm.setInt(2,aid);
            pstm.setInt(3,note);
            pstm.setString(4,kommentar);


            pstm.executeUpdate();
            complete();
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
